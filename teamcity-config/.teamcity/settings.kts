import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/**
 * TeamCity Configuration as Code Demo Project - Python Flask Application
 * 
 * This file demonstrates how to define TeamCity projects using Kotlin DSL.
 * Benefits:
 * - Version controlled configuration
 * - Type-safe configuration
 * - IDE support with autocompletes
 * - Reusable configuration patterns
 */
version = "2023.05"

project {
    id = AbsoluteId("TeamOKITO")
    name = "TeamOKITO"
    description = "Demonstrating TeamCity Configuration as Code capabilities with a Python Flask application"
    
    // Build number format
    buildNumberPattern = "%build.counter%"
    
    // VCS Root - Git repository configuration
    vcsRoot {
        id = "GitRepo"
        name = "Git Repository"
        url = "https://github.com/ptokito/teamOKITO.git"
        branch = "refs/heads/main"
        branchSpec = "+:refs/heads/*"
        checkoutMode = CheckoutMode.ON_SERVER
    }
    
    // Build Configuration 1: Python Setup and Test
    buildType {
        id = "PythonSetupAndTest"
        name = "Python Setup and Test"
        description = "Sets up Python environment and runs tests for the Flask application"
        
        // VCS Settings
        vcs {
            root(AbsoluteId("GitRepo"))
            cleanCheckout = true
        }
        
        // Build Steps
        steps {
            // Python Environment Setup
            script {
                name = "Setup Python Environment"
                scriptContent = """
                    #!/bin/bash
                    echo "Setting up Python environment..."
                    python3 -m venv venv
                    source venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    echo "Python environment setup completed"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Run Python Tests
            script {
                name = "Run Python Tests"
                scriptContent = """
                    #!/bin/bash
                    echo "Running Python tests..."
                    source venv/bin/activate
                    python -m pytest tests/ -v --tb=short
                    echo "Tests completed"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Code Quality Check
            script {
                name = "Code Quality Check"
                scriptContent = """
                    #!/bin/bash
                    echo "Running code quality checks..."
                    source venv/bin/activate
                    pip install flake8 black
                    flake8 teamOKITO/ --max-line-length=100 --exclude=venv
                    black --check teamOKITO/
                    echo "Code quality checks completed"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Test Application Startup (Quick Test)
            script {
                name = "Test Application Startup"
                scriptContent = """
                    #!/bin/bash
                    echo "Testing Flask application startup..."
                    source venv/bin/activate
                    
                    # Test if the app can start (quick test)
                    timeout 30s python teamOKITO/app.py &
                    APP_PID=$!
                    
                    # Wait for app to start
                    sleep 5
                    
                    # Test if app is responding
                    if curl -f http://localhost:5000/ > /dev/null 2>&1; then
                        echo "Application started successfully"
                        kill $APP_PID 2>/dev/null
                        exit 0
                    else
                        echo "Application failed to start or respond"
                        kill $APP_PID 2>/dev/null
                        exit 1
                    fi
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
        }
        
        // Build Triggers
        triggers {
            vcs {
                branchFilter = "+:*"
                groupCheckinsByCommitter = true
                perCheckinTriggering = true
            }
        }
        
        // Build Features
        features {
            // Build History
            buildHistory {
                maxResults = 50
            }
            
            // Notifications
            notifications {
                notifierSettings = email {
                    recipients = "team@example.com"
                    notifyBuildStart = true
                    notifyBuildSuccess = true
                    notifyBuildFailure = true
                }
            }
        }
        
        // Parameters
        params {
            param("PYTHON_VERSION", "3.9")
            param("TEST_TIMEOUT", "300")
            param("CODE_QUALITY_ENABLED", "true")
        }
        
        // Failure Conditions
        failureConditions {
            executionTimeoutMin = 30
            nonZeroExitCode = false
        }
        
        // Dependencies
        dependencies {
            snapshot(RelativeId("PythonSetupAndTest")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
        
        // Trigger the next build in the pipeline
        triggers {
            vcs {
                branchFilter = "+:refs/heads/main"
                groupCheckinsByCommitter = true
                perCheckinTriggering = false
            }
        }
    }
    
    // Build Configuration 2: Flask Application Build
    buildType {
        id = "FlaskAppBuild"
        name = "Flask Application Build"
        description = "Builds and packages the Flask application for deployment"
        
        // VCS Settings
        vcs {
            root(AbsoluteId("GitRepo"))
            cleanCheckout = true
        }
        
        // Build Steps
        steps {
            // Setup Python Environment
            script {
                name = "Setup Python Environment"
                scriptContent = """
                    #!/bin/bash
                    echo "Setting up Python environment..."
                    python3 -m venv venv
                    source venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    echo "Python environment setup completed"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Build Application
            script {
                name = "Build Flask Application"
                scriptContent = """
                    #!/bin/bash
                    echo "Building Flask application..."
                    source venv/bin/activate
                    
                    # Create build directory
                    mkdir -p build
                    
                    # Copy application files
                    cp -r teamOKITO/* build/
                    cp teamOKITO/requirements.txt build/
                    
                    # Create deployment package
                    cd build
                    tar -czf ../flask-app-${BUILD_NUMBER}.tar.gz .
                    cd ..
                    
                    echo "Flask application build completed"
                    echo "Package: flask-app-${BUILD_NUMBER}.tar.gz"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Test Application Startup
            script {
                name = "Test Application Startup"
                scriptContent = """
                    #!/bin/bash
                    echo "Testing Flask application startup..."
                    source venv/bin/activate
                    cd build
                    
                    # Test if the app can start
                    timeout 30s python app.py &
                    APP_PID=$!
                    
                    # Wait for app to start
                    sleep 5
                    
                    # Test health endpoint
                    if curl -f http://localhost:5000/health; then
                        echo "Application started successfully"
                        kill $APP_PID
                        exit 0
                    else
                        echo "Application failed to start"
                        kill $APP_PID
                        exit 1
                    fi
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
        }
        
        // Build Triggers
        triggers {
            vcs {
                branchFilter = "+:refs/heads/main"
                groupCheckinsByCommitter = true
                perCheckinTriggering = false
            }
        }
        
        // Build Features
        features {
            // Build History
            buildHistory {
                maxResults = 30
            }
            
            // Notifications
            notifications {
                notifierSettings = email {
                    recipients = "devops@example.com"
                    notifyBuildStart = true
                    notifyBuildSuccess = true
                    notifyBuildFailure = true
                }
            }
        }
        
        // Dependencies
        dependencies {
            snapshot(RelativeId("PythonSetupAndTest")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
    
    // Build Configuration 3: Deploy to Render
    buildType {
        id = "DeployToRender"
        name = "Deploy to Render"
        description = "Deploys the Flask application to Render hosting platform"
        
        // VCS Settings
        vcs {
            root(AbsoluteId("GitRepo"))
            cleanCheckout = true
        }
        
        // Build Steps
        steps {
            // Setup Python Environment
            script {
                name = "Setup Python Environment"
                scriptContent = """
                    #!/bin/bash
                    echo "Setting up Python environment..."
                    python3 -m venv venv
                    source venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                    echo "Python environment setup completed"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
            
            // Deploy to Render via Deploy Hook
            script {
                name = "Deploy to Render via Deploy Hook"
                scriptContent = """
                    #!/bin/bash
                    echo "Deploying to Render via deploy hook..."
                    source venv/bin/activate
                    
                    # Set deployment environment variables
                    export RENDER_DEPLOY_HOOK="${RENDER_DEPLOY_HOOK}"
                    
                    echo "Build Number: ${BUILD_NUMBER}"
                    echo "Deployment Date: $(date -u +"%Y-%m-%dT%H:%M:%SZ")"
                    
                    # Trigger Render deployment via deploy hook
                    echo "Triggering Render deployment..."
                    DEPLOY_RESPONSE=$(curl -s -X POST "${RENDER_DEPLOY_HOOK}")
                    
                    if [ $? -eq 0 ]; then
                        echo "Deployment triggered successfully!"
                        echo "Response: ${DEPLOY_RESPONSE}"
                        
                        # Extract deployment ID from response
                        DEPLOY_ID=$(echo "${DEPLOY_RESPONSE}" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
                        if [ ! -z "${DEPLOY_ID}" ]; then
                            echo "Deployment ID: ${DEPLOY_ID}"
                        fi
                    else
                        echo "Failed to trigger deployment"
                        exit 1
                    fi
                    
                    echo "Deployment to Render completed successfully"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
        }
        
        // Build Triggers
        triggers {
            vcs {
                branchFilter = "+:refs/heads/main"
                groupCheckinsByCommitter = true
                perCheckinTriggering = false
            }
        }
        
        // Build Features
        features {
            // Build History
            buildHistory {
                maxResults = 20
            }
            
            // Notifications
            notifications {
                notifierSettings = email {
                    recipients = "devops@example.com"
                    notifyBuildStart = true
                    notifyBuildSuccess = true
                    notifyBuildFailure = true
                }
            }
        }
        
        // Dependencies
        dependencies {
            snapshot(RelativeId("FlaskAppBuild")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
        
        // Trigger the next build in the pipeline
        triggers {
            vcs {
                branchFilter = "+:refs/heads/main"
                groupCheckinsByCommitter = true
                perCheckinTriggering = false
            }
        }
        
        // Parameters for Render deployment
        params {
            param("RENDER_DEPLOY_HOOK", "https://api.render.com/deploy/srv-d2ni6i7fte5s739g34q0?key=hLoCv29o1Ew")
            param("DEPLOYMENT_ENVIRONMENT", "production")
        }
    }
    
    // Main Pipeline Build Configuration
    buildType {
        id = "FullPipeline"
        name = "Full CI/CD Pipeline"
        description = "Runs the complete pipeline: Test → Build → Deploy to Render"
        
        // VCS Settings
        vcs {
            root(AbsoluteId("GitRepo"))
            cleanCheckout = true
        }
        
        // Build Steps - This build just triggers the pipeline
        steps {
            script {
                name = "Pipeline Orchestration"
                scriptContent = """
                    #!/bin/bash
                    echo "Starting full CI/CD pipeline..."
                    echo "Build Number: ${BUILD_NUMBER}"
                    echo "Pipeline will run:"
                    echo "1. Python Setup and Test"
                    echo "2. Flask Application Build"
                    echo "3. Deploy to Render"
                    echo ""
                    echo "Pipeline orchestration completed successfully!"
                """.trimIndent()
                scriptExecMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            }
        }
        
        // Build Triggers - This is the main entry point
        triggers {
            vcs {
                branchFilter = "+:refs/heads/main"
                groupCheckinsByCommitter = true
                perCheckinTriggering = true
            }
        }
        
        // Build Features
        features {
            // Build History
            buildHistory {
                maxResults = 20
            }
            
            // Notifications
            notifications {
                notifierSettings = email {
                    recipients = "devops@example.com"
                    notifyBuildStart = true
                    notifyBuildSuccess = true
                    notifyBuildFailure = true
                }
            }
        }
        
        // Dependencies - This triggers the entire pipeline
        dependencies {
            snapshot(RelativeId("PythonSetupAndTest")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
}
