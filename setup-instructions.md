# TeamCity Configuration as Code Demo Setup Instructions

This document provides step-by-step instructions for setting up and running the Configuration as Code demo in TeamCity.

## 🎯 Prerequisites

### System Requirements
- **TeamCity**: Professional or Enterprise edition (2022.10+ recommended)
- **Java**: JDK 11 or higher
- **Git**: Version control system
- **Maven**: Build tool (3.6+ recommended)

### TeamCity Setup
1. **Install TeamCity** following the [official installation guide](https://www.jetbrains.com/help/teamcity/installation.html)
2. **Enable Configuration as Code**:
   - Go to Administration → Global Settings
   - Enable "Configuration as Code" feature
   - Set up VCS root for configuration repository

## 🚀 Step-by-Step Setup

### Step 1: Prepare the Demo Repository

1. **Clone or create the demo repository**:
   ```bash
   git clone <your-repo-url>
   cd teamcity-cac-demo
   ```

2. **Verify the structure**:
   ```
   ├── sample-project/          # Java application
   ├── teamcity-config/         # TeamCity configuration files
   ├── presentation/            # Presentation materials
   └── README.md               # This file
   ```

### Step 2: Configure TeamCity Project

1. **Create a new project in TeamCity**:
   - Go to Administration → Projects
   - Click "Create Project"
   - Choose "From a repository URL"
   - Enter your repository URL

2. **Set up VCS Root**:
   - In the project settings, go to "VCS Roots"
   - Create a new VCS root pointing to your demo repository
   - Set branch specification to `+:*` for all branches

3. **Enable Configuration as Code**:
   - In project settings, go to "Configuration as Code"
   - Enable "Store project settings in VCS"
   - Set the path to `.teamcity` directory

### Step 3: Import Configuration

1. **Upload configuration files**:
   - Copy the `.teamcity` directory to your repository
   - Commit and push the changes
   - TeamCity will automatically detect and import the configuration

2. **Verify configuration import**:
   - Check that all build configurations are created
   - Verify VCS roots are properly configured
   - Ensure build steps are correctly set up

### Step 4: Configure Build Agents

1. **Set up build agents**:
   - Go to Administration → Agents
   - Ensure you have at least one build agent available
   - Verify Java and Maven are installed on the agent

2. **Install required tools**:
   ```bash
   # On the build agent
   sudo apt-get update
   sudo apt-get install openjdk-11-jdk maven git
   ```

### Step 5: Test the Configuration

1. **Run a test build**:
   - Go to your project in TeamCity
   - Click "Run" on the "Build and Test" build configuration
   - Monitor the build progress

2. **Verify build steps**:
   - Check that Maven goals are executed correctly
   - Verify tests are running
   - Ensure build artifacts are generated

## 🔧 Configuration Customization

### Modifying Build Configurations

1. **Edit Kotlin DSL files**:
   ```kotlin
   // In .teamcity/Project.kt
   buildType {
       id = "CustomBuild"
       name = "Custom Build Configuration"
       // Add your custom configuration here
   }
   ```

2. **Add new build steps**:
   ```kotlin
   steps {
       maven {
           name = "Custom Maven Goal"
           goals = "your-custom-goal"
       }
   }
   ```

3. **Configure notifications**:
   ```kotlin
   features {
       notifications {
           notifierSettings = email {
               recipients = "your-team@company.com"
           }
       }
   }
   ```

### Environment-Specific Configuration

1. **Create environment variables**:
   ```kotlin
   parameters {
       param("ENVIRONMENT", "development")
       param("DATABASE_URL", "jdbc:mysql://localhost:3306/dev")
   }
   ```

2. **Use configuration parameters**:
   ```kotlin
   maven {
       goals = "%MAVEN_GOALS%"
       jvmArgs = "-Xmx%JVM_MEMORY%"
   }
   ```

## 📊 Monitoring and Maintenance

### Build Monitoring

1. **Check build history**:
   - Monitor build success/failure rates
   - Track build duration trends
   - Identify performance bottlenecks

2. **Review logs**:
   - Check build step logs for errors
   - Monitor resource usage
   - Verify configuration changes

### Configuration Updates

1. **Making changes**:
   - Edit configuration files in your IDE
   - Commit changes to version control
   - Push to trigger automatic import

2. **Rollback procedures**:
   - Use Git to revert to previous configuration
   - Push rollback changes
   - Verify configuration is restored

## 🚨 Troubleshooting

### Common Issues

1. **Configuration not imported**:
   - Check VCS root configuration
   - Verify `.teamcity` directory path
   - Ensure Configuration as Code is enabled

2. **Build failures**:
   - Check build agent requirements
   - Verify tool installations
   - Review build step logs

3. **VCS issues**:
   - Check repository permissions
   - Verify branch specifications
   - Ensure clean checkout is working

### Debug Steps

1. **Enable debug logging**:
   - Go to Administration → Global Settings
   - Set logging level to DEBUG
   - Check TeamCity logs for detailed information

2. **Verify file permissions**:
   - Ensure TeamCity can read configuration files
   - Check VCS repository access
   - Verify build agent permissions

## 📈 Best Practices

### Configuration Management

1. **Version control everything**:
   - Store all configuration in Git
   - Use meaningful commit messages
   - Tag releases for easy rollback

2. **Use templates**:
   - Create reusable configuration patterns
   - Parameterize common settings
   - Document configuration options

3. **Test configurations**:
   - Use feature branches for testing
   - Validate configurations before production
   - Monitor configuration changes

### Team Collaboration

1. **Code review process**:
   - Require pull requests for configuration changes
   - Review configuration modifications
   - Test changes in staging environment

2. **Documentation**:
   - Document configuration patterns
   - Maintain change logs
   - Provide setup guides for new team members

## 🔄 Migration from Manual Configuration

### Assessment Phase

1. **Inventory existing configurations**:
   - Document all build configurations
   - Identify configuration patterns
   - Assess complexity and dependencies

2. **Choose migration approach**:
   - Start with simple configurations
   - Use gradual migration strategy
   - Plan rollback procedures

### Implementation Phase

1. **Create configuration files**:
   - Convert existing configurations to code
   - Test in development environment
   - Validate with team members

2. **Deploy to production**:
   - Use feature flags for gradual rollout
   - Monitor system performance
   - Gather feedback from users

## 📚 Additional Resources

### Documentation
- [TeamCity Configuration as Code](https://www.jetbrains.com/help/teamcity/configuration-as-code.html)
- [Kotlin DSL Reference](https://www.jetbrains.com/help/teamcity/kotlin-dsl.html)
- [Build Configuration](https://www.jetbrains.com/help/teamcity/build-configuration.html)

### Community Support
- [TeamCity Community](https://teamcity-support.jetbrains.com/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/teamcity)
- [GitHub Examples](https://github.com/JetBrains/teamcity-configs)

---

## 🎯 Next Steps

After completing the setup:

1. **Run the demo** for your team
2. **Customize configurations** for your specific needs
3. **Implement in production** following best practices
4. **Share knowledge** with other teams
5. **Contribute** to the Configuration as Code community

---

*For additional support or questions, please refer to the TeamCity documentation or community forums.*
