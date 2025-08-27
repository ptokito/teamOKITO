import unittest
import json
import os
import sys
from unittest.mock import patch

# Add the sample-project directory to the path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

from app import app

class ConfigurationAsCodeDemoTestCase(unittest.TestCase):
    def setUp(self):
        self.app = app.test_client()
        self.app.testing = True

    def test_index_page(self):
        """Test that the index page loads successfully"""
        response = self.app.get('/')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Configuration as Code Demo', response.data)
        self.assertIn(b'CI/CD Pipeline Successfully Deployed', response.data)

    def test_build_status_api(self):
        """Test the build status API endpoint"""
        response = self.app.get('/api/build-status')
        self.assertEqual(response.status_code, 200)
        
        data = json.loads(response.data)
        self.assertIn('status', data)
        self.assertIn('build_number', data)
        self.assertIn('pipeline_steps', data)
        self.assertEqual(data['status'], 'success')

    def test_configuration_api(self):
        """Test the configuration API endpoint"""
        response = self.app.get('/api/configuration')
        self.assertEqual(response.status_code, 200)
        
        data = json.loads(response.data)
        self.assertIn('project_id', data)
        self.assertIn('build_types', data)
        self.assertIn('vcs_roots', data)

    def test_health_check(self):
        """Test the health check endpoint"""
        response = self.app.get('/health')
        self.assertEqual(response.status_code, 200)
        
        data = json.loads(response.data)
        self.assertEqual(data['status'], 'healthy')
        self.assertIn('timestamp', data)
        self.assertIn('version', data)

    def test_build_info_environment_variables(self):
        """Test that build info is properly set from environment variables"""
        with patch.dict(os.environ, {
            'BUILD_NUMBER': 'TEST-123',
            'DEPLOYMENT_DATE': '2024-01-01',
            'REPOSITORY': 'test-repo',
            'CONFIGURATION': 'test-config'
        }):
            # Reimport app to get fresh environment variables
            import importlib
            import app
            importlib.reload(app)
            
            response = self.app.get('/')
            self.assertIn(b'TEST-123', response.data)
            self.assertIn(b'2024-01-01', response.data)

    def test_pipeline_steps_display(self):
        """Test that all pipeline steps are displayed"""
        response = self.app.get('/')
        self.assertIn(b'Build', response.data)
        self.assertIn(b'Test', response.data)
        self.assertIn(b'Package', response.data)
        self.assertIn(b'Deploy', response.data)

    def test_tech_tags_display(self):
        """Test that technology tags are displayed"""
        response = self.app.get('/')
        self.assertIn(b'TeamCity CI/CD', response.data)
        self.assertIn(b'Render Hosting', response.data)
        self.assertIn(b'Python Flask', response.data)

if __name__ == '__main__':
    unittest.main()
