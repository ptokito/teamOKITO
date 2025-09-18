from flask import Flask, render_template, jsonify
import os
from datetime import datetime

#comment 
app = Flask(__name__)


class BuildInfo:
    def __init__(self):
        self.build_number = os.getenv('BUILD_NUMBER', 'Local Dev')
        self.deployment_date = os.getenv('DEPLOYMENT_DATE', 'Unknown')
        self.repository = os.getenv('REPOSITORY', 'github.com/ptokito/teamOKITO')
        self.configuration = os.getenv('CONFIGURATION', '.teamcity/settings.kts')


class PipelineStep:
    def __init__(self, name, icon, status="success"):
        self.name = name
        self.icon = icon
        self.status = status


class DeploymentPipeline:
    def __init__(self):
        self.steps = [
            PipelineStep("Build", "🔨", "success"),
            PipelineStep("Test", "🧪", "success"),
            PipelineStep("Package", "📦", "success"),
            PipelineStep("Deploy", "🚀", "success")
        ]


@app.route('/')
def index():
    build_info = BuildInfo()
    pipeline = DeploymentPipeline()

    return render_template('index.html',
                         build_info=build_info,
                         pipeline=pipeline)


@app.route('/api/build-status')
def build_status():
    """API endpoint to get current build status"""
    try:
        # Simulate checking on the build status
        # Configuration as Code Demo - TeamCity Pipeline Integration
        # In a real implementation, this would query TeamCity API
        status = {
            "status": "success",
            "last_build": datetime.now().isoformat(),
            "build_number": os.getenv('BUILD_NUMBER', 'Local Dev'),
            "pipeline_steps": [
                {"name": "Build", "status": "success", "duration": "45s"},
                {"name": "Test", "status": "success", "duration": "1m 23s"},
                {"name": "Package", "status": "success", "duration": "12s"},
                {"name": "Deploy", "status": "success", "duration": "2m 15s"}
            ]
        }
        return jsonify(status)
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/api/configuration')
def get_configuration():
    """API endpoint to get current configuration"""
    try:
        # In a real implementation, this would read from .teamcity directory
        config = {
            "project_id": "ConfigurationAsCodeDemo",
            "project_name": "Configuration as Code Demo",
            "build_types": [
                {
                    "id": "BuildAndTest",
                    "name": "Build and Test",
                    "description": "Compiles the project and runs tests"
                },
                {
                    "id": "CodeQuality",
                    "name": "Code Quality Analysis",
                    "description": "Runs static code analysis and generates reports"
                },
                {
                    "id": "PackageAndDeploy",
                    "name": "Package and Deploy",
                    "description": "Creates deployment package and deploys to staging"
                }
            ],
            "vcs_roots": [
                {
                    "id": "GitRepo",
                    "name": "Git Repository",
                    "url": "https://github.com/ptokito/teamOKITO"
                }
            ]
        }
        return jsonify(config)
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/health')
def health_check():
    """Health check endpoint for monitoring"""
    return jsonify({
        "status": "healthy",
        "timestamp": datetime.now().isoformat(),
        "version": "1.0.0"
    })


if __name__ == '__main__':
    # Set environment variables for demo purposes
    os.environ['BUILD_NUMBER'] = os.getenv('BUILD_NUMBER', 'Local Dev')
    os.environ['DEPLOYMENT_DATE'] = os.getenv('DEPLOYMENT_DATE', 'Unknown')
    os.environ['REPOSITORY'] = os.getenv('REPOSITORY', 'github.com/ptokito/teamcityjava')
    os.environ['CONFIGURATION'] = os.getenv('CONFIGURATION', '.teamcity/settings.kts')

    app.run(debug=True, host='0.0.0.0', port=5000)
