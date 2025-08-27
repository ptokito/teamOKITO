# TeamCity Configuration as Code (CaC) Demo

This repository demonstrates the Configuration as Code capabilities of TeamCity, showcasing how CI/CD configurations can be stored, versioned, and managed as code.

## 🎯 Demo Overview

This demo is designed for both **non-technical stakeholders** (Product Managers, Business Analysts) and **technical teams** (DevOps Engineers, Developers) to understand the benefits and implementation of Configuration as Code.

## 📁 Repository Structure

```
├── README.md                           # This file
├── presentation/                       # Presentation materials
│   ├── Configuration-as-Code-Demo.md   # Main presentation
│   ├── demo-script.md                  # Live demo script
│   └── configuration-approaches-comparison.md  # Approach comparison
├── sample-project/                     # Python Flask application
│   ├── app.py                         # Main Flask application
│   ├── test_app.py                    # Unit tests
│   ├── requirements.txt                # Python dependencies
│   └── templates/                      # HTML templates
│       └── index.html                  # Main web interface
├── teamcity-config/                    # TeamCity configuration files
│   ├── .teamcity/                     # Project configuration (Kotlin DSL)
│   └── build-configs/                 # Alternative configuration formats
└── setup-instructions.md              # Detailed setup guide
```

## 🚀 Quick Start

1. **Clone this repository** to your local machine
2. **Review the presentation** in `presentation/Configuration-as-Code-Demo.md`
3. **Set up TeamCity** following `setup-instructions.md`
4. **Import the configuration** using the provided TeamCity files
5. **Run the demo** to see Configuration as Code in action

## 🐍 Python Application

The demo includes a **Python Flask web application** that demonstrates:
- **Modern web interface** replicating the CI/CD dashboard
- **API endpoints** for build status and configuration
- **Interactive elements** showing pipeline steps
- **Real-time updates** and status monitoring

### Running the Python App Locally

```bash
cd sample-project
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
python app.py
```

Then open http://localhost:5000 in your browser to see the demo interface.

## 🎭 Target Audience

### Non-Technical Stakeholders
- **Product Managers**: Understand business benefits and ROI
- **Business Analysts**: See process improvements and efficiency gains
- **Project Managers**: Learn about risk reduction and compliance

### Technical Teams
- **DevOps Engineers**: Implementation details and best practices
- **Developers**: How to work with CaC in daily development
- **System Administrators**: Infrastructure and maintenance considerations

## 🔧 Prerequisites

- TeamCity Professional or Enterprise (2022.10+ recommended)
- Python 3.8+ (for the sample application)
- Git for version control
- Basic understanding of CI/CD concepts

## 📚 What You'll Learn

- **Benefits of Configuration as Code**
- **Implementation approaches** in TeamCity**
- **Best practices** and common patterns
- **Real-world examples** and use cases
- **Migration strategies** from manual configuration

## 🆘 Getting Help

If you encounter any issues during the demo setup:
1. Check the `setup-instructions.md` file
2. Review TeamCity's official documentation
3. Contact your TeamCity administrator

---

*This demo was created to showcase TeamCity's Configuration as Code capabilities and best practices.*
