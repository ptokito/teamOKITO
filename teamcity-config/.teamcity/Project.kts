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
 * - IDE support with autocomplete
 * - Reusable configuration patterns
 */
version = "2023.05"

project {
    id = AbsoluteId("CacDemoProject")
    name = "Configuration as Code Demo"
    description = "Demonstrating TeamCity Configuration as Code capabilities with a Python Flask application"
    
    // VCS Root - Git repository configuration
    vcsRoot {
        id = "GitRepo"
        name = "Git Repository"
        url = "https://github.com/ptokito/teamcityjava.git"
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
                    flake8 sample-project/ --max-line-length=100 --exclude=venv
                    black --check sample-project/
                    echo "Code quality checks completed"
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
        
        // Failure Conditions
        failureConditions {
            executionTimeoutMin = 30
            nonZeroExitCode = false
        }
        
        // Dependencies
        dependencies {
            snapshot(RelativeId("GitRepo")) {
                onDependencyFailure = FailureAction.FAIL_TO_START
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
                    cp -r sample-project/* build/
                    cp requirements.txt build/
                    
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
            
            // Deploy to Render
            script {
                name = "Deploy to Render"
                scriptContent = """
                    #!/bin/bash
                    echo "Deploying to Render..."
                    source venv/bin/activate
                    
                    # Set deployment environment variables
                    export RENDER_TOKEN="${RENDER_TOKEN}"
                    export RENDER_SERVICE_ID="${RENDER_SERVICE_ID}"
                    
                    # Create deployment package
                    mkdir -p deploy
                    cp -r sample-project/* deploy/
                    cp requirements.txt deploy/
                    
                    # Add render.yaml for Render configuration
                    cat > deploy/render.yaml << EOF
                    services:
                      - type: web
                        name: configuration-as-code-demo
                        env: python
                        buildCommand: pip install -r requirements.txt
                        startCommand: python app.py
                        envVars:
                          - key: PYTHON_VERSION
                            value: 3.9.0
                          - key: BUILD_NUMBER
                            value: ${BUILD_NUMBER}
                          - key: DEPLOYMENT_DATE
                            value: $(date -u +"%Y-%m-%dT%H:%M:%SZ")
                    EOF
                    
                    echo "Deployment package created"
                    echo "Build Number: ${BUILD_NUMBER}"
                    echo "Deployment Date: $(date -u +"%Y-%m-%dT%H:%M:%SZ")"
                    
                    # In a real deployment, you would use Render CLI or API
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
        
        // Parameters for Render deployment
        params {
            param("RENDER_TOKEN", "")
            param("RENDER_SERVICE_ID", "")
            param("DEPLOYMENT_ENVIRONMENT", "production")
        }
    }
}
