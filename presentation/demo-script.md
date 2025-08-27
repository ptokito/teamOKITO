# Live Demo Script: TeamCity Configuration as Code

This script provides a step-by-step guide for conducting a live demonstration of TeamCity's Configuration as Code capabilities.

## 🎬 Demo Overview

**Duration**: 30-45 minutes  
**Audience**: Mixed (Technical and Non-Technical)  
**Format**: Live demonstration with interactive elements  

---

## 🎯 Demo Objectives

1. **Show the problem** of manual configuration management
2. **Demonstrate the solution** using Configuration as Code
3. **Highlight benefits** for both technical and business stakeholders
4. **Provide hands-on experience** with the technology

---

## 📋 Pre-Demo Setup

### Required Materials
- [ ] TeamCity instance with Configuration as Code enabled
- [ ] Demo repository cloned and configured
- [ ] Sample project ready for building
- [ ] Backup of current configuration (if applicable)
- [ ] Presentation slides ready
- [ ] Demo environment tested and working

### Audience Preparation
- [ ] Brief overview of CI/CD concepts (if needed)
- [ ] Explain what to expect during the demo
- [ ] Encourage questions and interaction
- [ ] Set context for both technical and non-technical stakeholders

---

## 🎭 Demo Flow

### Phase 1: Setting the Stage (5 minutes)

#### Opening Statement
> "Today, we're going to explore how Configuration as Code transforms the way organizations manage their CI/CD systems. We'll see both the challenges of manual configuration and the solutions that Configuration as Code provides."

#### Current State Discussion
- **Ask the audience**: "How many of you have experienced configuration drift or deployment failures?"
- **Show statistics**: "According to industry data, 60% of deployment failures are caused by configuration issues"
- **Present the problem**: Manual configuration management leads to:
  - Inconsistencies between environments
  - Human errors during configuration changes
  - Difficult troubleshooting and rollback
  - Compliance and audit challenges

### Phase 2: Problem Demonstration (10 minutes)

#### Show Manual Configuration Issues
1. **Navigate to TeamCity UI**:
   - Go to a project with manual configuration
   - Show build configuration settings
   - Highlight the manual nature of changes

2. **Demonstrate Configuration Drift**:
   - Show different settings between similar projects
   - Point out inconsistencies in build steps
   - Explain how this happens over time

3. **Simulate a Configuration Change**:
   - Make a small change in the UI
   - Show that the change is not tracked
   - Explain the risk of undocumented changes

#### Interactive Element
> "Let's say we need to add a new build step. In the traditional approach, I would go to the UI, make the change, and hope I remember to document it. Sound familiar?"

---

### Phase 3: Solution Introduction (15 minutes)

#### Configuration as Code Overview
1. **Explain the Concept**:
   - "Configuration as Code treats your CI/CD settings like application code"
   - "Everything is stored in version control with full audit trail"
   - "Changes go through the same review process as code changes"

2. **Show the Repository Structure**:
   - Navigate to the demo repository
   - Show the `.teamcity` directory
   - Explain the different configuration formats

#### Live Configuration Demonstration
1. **Show Kotlin DSL Configuration**:
   ```kotlin
   // Open .teamcity/Project.kt
   buildType {
       id = "BuildAndTest"
       name = "Build and Test"
       // Explain the structure
   }
   ```

2. **Explain the Benefits**:
   - Type-safe configuration
   - IDE support with autocomplete
   - Version control integration
   - Reusable patterns

3. **Show Alternative Formats**:
   - XML configuration (familiar to many developers)
   - YAML configuration (human-readable)
   - Explain when to use each

### Phase 4: Live Configuration Change (10 minutes)

#### Making a Configuration Change
1. **Edit Configuration File**:
   - Open the configuration file in an IDE
   - Make a visible change (e.g., add a new build step)
   - Show the change in the editor

2. **Version Control Process**:
   - Commit the change with a meaningful message
   - Push to the repository
   - Show the Git history

3. **Automatic TeamCity Update**:
   - Navigate back to TeamCity
   - Show the automatic configuration import
   - Verify the change is applied

#### Interactive Element
> "Now let's see what happens when I push this change. Watch how TeamCity automatically picks up the new configuration without any manual intervention."

---

### Phase 5: Benefits Demonstration (10 minutes)

#### Show Key Benefits
1. **Version Control and Audit Trail**:
   - Show Git history of configuration changes
   - Explain how this helps with compliance
   - Demonstrate rollback capability

2. **Consistency Across Environments**:
   - Show how the same configuration applies everywhere
   - Explain environment parity benefits
   - Demonstrate quick environment replication

3. **Team Collaboration**:
   - Show pull request workflow for configuration changes
   - Explain code review process
   - Highlight knowledge sharing benefits

#### Real-World Scenarios
1. **Disaster Recovery**:
   - "What if our TeamCity server goes down?"
   - Show how configuration can be restored from Git
   - Demonstrate the speed of recovery

2. **Environment Scaling**:
   - "What if we need to set up a new environment?"
   - Show how configuration can be cloned
   - Explain the time savings

---

### Phase 6: Business Impact (5 minutes)

#### Quantify the Benefits
1. **Cost Savings**:
   - Reduced manual configuration time
   - Fewer deployment failures
   - Faster environment setup

2. **Risk Reduction**:
   - Eliminated configuration drift
   - Full audit trail for compliance
   - Quick rollback capability

3. **Operational Efficiency**:
   - Faster deployments
   - Consistent environments
   - Better team productivity

#### Interactive Discussion
> "For the business stakeholders in the room: What would it mean to your organization if you could eliminate 60% of deployment failures? What's the cost of a single production outage?"

---

## 🎯 Demo Scenarios

### Scenario 1: Adding a New Build Step
**Setup**: Have a simple configuration ready  
**Action**: Add a new Maven goal to the build  
**Demonstration**: Show the entire process from code change to TeamCity update  

### Scenario 2: Environment Configuration
**Setup**: Show different environment settings  
**Action**: Modify environment-specific parameters  
**Demonstration**: Show how changes propagate across environments  

### Scenario 3: Rollback Demonstration
**Setup**: Make a configuration change that "breaks" something  
**Action**: Show the rollback process  
**Demonstration**: Demonstrate quick recovery using Git  

---

## ❓ Interactive Elements

### Questions for Non-Technical Stakeholders
- "How much time does your team spend on configuration management?"
- "What's the cost of a production deployment failure?"
- "How do you currently ensure compliance with configuration changes?"

### Questions for Technical Teams
- "How do you currently manage configuration drift?"
- "What's your process for rolling back configuration changes?"
- "How do you ensure consistency across environments?"

### Group Discussion Points
- "What are the biggest challenges you face with CI/CD configuration?"
- "How would Configuration as Code impact your current workflow?"
- "What would be the first project you'd want to migrate?"

---

## 🚨 Handling Demo Issues

### Common Problems and Solutions

1. **Configuration Import Fails**:
   - **Problem**: TeamCity doesn't pick up configuration changes
   - **Solution**: Check VCS root settings and Configuration as Code enablement
   - **Backup**: Have screenshots ready to show the process

2. **Build Fails During Demo**:
   - **Problem**: Sample project doesn't build successfully
   - **Solution**: Have a working backup configuration ready
   - **Backup**: Use pre-recorded video if necessary

3. **Network Issues**:
   - **Problem**: Can't access TeamCity or repository
   - **Solution**: Have offline demos and screenshots prepared
   - **Backup**: Use local TeamCity instance if possible

### Demo Recovery Strategies
- **Keep backup materials ready**: Screenshots, videos, alternative demos
- **Practice the demo multiple times**: Ensure smooth execution
- **Have fallback scenarios**: Different ways to demonstrate the same concept
- **Maintain audience engagement**: Keep them involved even if technology fails

---

## 🎯 Demo Conclusion

### Key Takeaways
1. **Configuration as Code** transforms manual, error-prone processes into automated, reliable workflows
2. **Version control** provides full audit trail and rollback capability
3. **Consistency** eliminates configuration drift across environments
4. **Team collaboration** improves through code review and knowledge sharing

### Call to Action
- **For technical teams**: Start with a pilot project and gradually expand
- **For business stakeholders**: Support the initiative and measure the ROI
- **For everyone**: Begin the journey toward more reliable, efficient CI/CD

### Next Steps
1. **Set up a pilot project** using the provided examples
2. **Train your team** on Configuration as Code concepts
3. **Measure the impact** on your development velocity
4. **Share success stories** with other teams

---

## 📚 Demo Materials Checklist

### Required Files
- [ ] Demo repository with working configuration
- [ ] Sample Java project ready to build
- [ ] TeamCity instance configured and accessible
- [ ] Backup configurations and screenshots
- [ ] Presentation slides

### Optional Materials
- [ ] Pre-recorded demo videos
- [ ] Configuration templates for different scenarios
- [ ] Migration planning documents
- [ ] ROI calculation examples
- [ ] Best practices guide

---

## 🎭 Presentation Tips

### Engaging the Audience
- **Start with a story**: Real-world scenario that resonates
- **Use analogies**: "Think of it like recipe management..."
- **Interactive elements**: Ask questions, encourage participation
- **Visual demonstrations**: Show, don't just tell

### Managing Time
- **Stick to the schedule**: Don't let one section run over
- **Have time checkpoints**: "We have 10 minutes left for..."
- **Flexible content**: Be ready to adjust based on audience interest
- **Clear transitions**: Move smoothly between sections

### Handling Questions
- **Encourage questions**: Make the audience feel comfortable
- **Defer complex questions**: "Great question, let's discuss that after the demo"
- **Use questions as teaching moments**: Turn questions into learning opportunities
- **Have backup resources**: Know where to find answers you don't have

---

*Remember: The goal is not just to show the technology, but to demonstrate how it solves real business problems and improves team productivity.*
