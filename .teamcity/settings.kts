// Required imports for TeamCity's Kotlin DSL API

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.*
import jetbrains.buildServer.configs.kotlin.triggers.*
import jetbrains.buildServer.configs.kotlin.vcs.*

version = "2025.07"


//Project Definition
// This creates the top-level TeamCity project container

project {
    name = "TeamOKITO"
    description = "Configuration as Code Demo - Single CI/CD Pipeline"
    
    vcsRoot(GitRepo)
    buildType(FullCiCdPipeline)
}
// VCS Root Definition - defines HOW TeamCity connects to source control
object GitRepo : GitVcsRoot({
    name = "Git Repository"
    url = "https://github.com/ptokito/teamOKITO.git"
    branch = "refs/heads/main"
    branchSpec = "+:refs/heads/*"
})
// Build Configuration Definition - defines WHAT happens when a build runs
// This creates the actual CI/CD pipeline with all steps and settings
object FullCiCdPipeline : BuildType({
    name = "Full CI/CD Pipeline"
    description = "Complete pipeline: Test → Build → Deploy to Render"
    
    buildNumberPattern = "%build.counter%"
    
    vcs {
        root(GitRepo)
        cleanCheckout = true
    }
    // Build steps - these execute in sequence, failing the build if any step fails
    steps {
        script {
            name = "1. Setup Python Environment"
            scriptContent = """
                #!/bin/bash
                echo "=== STEP 1: Setting up Python environment ==="
                python3 -m venv venv
                source venv/bin/activate
                pip install --upgrade pip
                pip install -r requirements.txt
                echo "Python environment setup completed"
            """.trimIndent()
        }
        // STEP 2: Test Execution
        script {
            name = "2. Run Tests"
            scriptContent = """
                #!/bin/bash
                echo "=== STEP 2: Running tests ==="
                source venv/bin/activate
                
                if [ -d "teamOKITO/tests" ] && [ "$(ls -A teamOKITO/tests)" ]; then
                    echo "Tests directory found, running tests..."
                    python -m pytest teamOKITO/tests/ -v --tb=short
                else
                    echo "No tests directory found - skipping tests for demo"
                fi
                echo "Test step completed"
            """.trimIndent()
        }
        // STEP 3: Application Smoke Test
        script {
            name = "3. Test Application Startup"
            scriptContent = """
                #!/bin/bash
                echo "=== STEP 3: Testing Flask application startup ==="
                source venv/bin/activate
                
                timeout 30s python app.py &
                APP_PID=${'$'}!
                sleep 5
                
                if curl -f http://localhost:5000/ > /dev/null 2>&1; then
                    echo "Application started successfully"
                    kill ${'$'}APP_PID 2>/dev/null
                else
                    echo "Application failed to start"
                    kill ${'$'}APP_PID 2>/dev/null
                    exit 1
                fi
            """.trimIndent()
        }
        // STEP 4: Build Artifact Creation
        script {
            name = "4. Build Application Package"
            scriptContent = """
                #!/bin/bash
                echo "=== STEP 4: Building application package ==="
                source venv/bin/activate
                
                mkdir -p build
                cp -r * build/ 2>/dev/null || true
                
                cd build
                tar -czf ../flask-app-%build.counter%.tar.gz .
                cd ..
                
                echo "Build package created: flask-app-%build.counter%.tar.gz"
            """.trimIndent()
        }
        // STEP 5: Deployment
        // Triggers deployment to Render platform using webhook API
        script {
            name = "5. Deploy to Render"
            scriptContent = """
                #!/bin/bash
                echo "=== STEP 5: Deploying to Render ==="
                
                echo "Build Number: %build.counter%"
                echo "Deployment Date: ${'$'}(date -u)"
                echo "Deploy Hook: %env.RENDER_DEPLOY_HOOK%"
                
                echo "Triggering Render deployment..."
                DEPLOY_RESPONSE=${'$'}(curl -s -X POST "%env.RENDER_DEPLOY_HOOK%")
                
                if [ ${'$'}? -eq 0 ]; then
                    echo "SUCCESS: Deployment triggered!"
                    echo "Response: ${'$'}DEPLOY_RESPONSE"
                    
                    DEPLOY_ID=${'$'}(echo "${'$'}DEPLOY_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
                    if [ ! -z "${'$'}DEPLOY_ID" ]; then
                        echo "Deployment ID: ${'$'}DEPLOY_ID"
                    fi
                    
                    echo "=== DEPLOYMENT TO RENDER COMPLETED SUCCESSFULLY ==="
                else
                    echo "ERROR: Failed to trigger deployment"
                    exit 1
                fi
            """.trimIndent()
        }
    }
    // Trigger Configuration - defines WHEN builds should run automatically
    triggers {
        vcs {
            branchFilter = "+:refs/heads/main"
            groupCheckinsByCommitter = true
            perCheckinTriggering = true
        }
    }
    // Build Parameters - variables available during build execution
    params {
        param("env.RENDER_DEPLOY_HOOK", "https://api.render.com/deploy/srv-d35nupvfte5s73936scg?key=6BA4VmLtYf0")
        param("DEPLOYMENT_ENVIRONMENT", "production")
    }
    
    failureConditions {
        executionTimeoutMin = 45
        nonZeroExitCode = true
    }
})
// Lets Go!!