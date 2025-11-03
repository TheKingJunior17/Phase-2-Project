# HW3 - Automated Testing System

## 🎯 Project Overview

This project implements a comprehensive automated testing system with 140 test cases across 5 major categories, demonstrating professional testing practices and Java 24 compatibility solutions.

## 📋 Table of Contents

- [Project Structure](#project-structure)
- [Test Implementation](#test-implementation)
- [Documentation](#documentation)
- [Java 24 Compatibility](#java-24-compatibility)
- [Quick Start](#quick-start)
- [Detailed Setup](#detailed-setup)
- [Test Execution](#test-execution)
- [Quality Metrics](#quality-metrics)
- [Troubleshooting](#troubleshooting)

## 🏗️ Project Structure

```
HW3/
├── src/
│   ├── main/java/edu/asu/cse360/hw3/
│   │   ├── entity/           # Data entities (User, Question, Answer)
│   │   ├── service/          # Business logic services
│   │   ├── security/         # Authentication and authorization
│   │   ├── validation/       # Input validation utilities
│   │   └── enums/           # Enumeration types
│   └── test/java/edu/asu/cse360/hw3/
│       ├── PasswordStrengthValidationTest.java
│       ├── UserAuthenticationIntegrationTest.java
│       ├── RoleBasedAccessControlTest.java
│       ├── QuestionSubmissionValidationTest.java
│       └── DatabaseCRUDOperationsTest.java
├── docs/
│   ├── javadoc/             # Generated API documentation
│   ├── TEST_DOCUMENTATION.md
│   └── TEST_EXECUTION_EVIDENCE.md
├── build.gradle             # Build configuration
└── README.md               # This file
```

## 🧪 Test Implementation

### Test Categories and Coverage

| Category | Test Class | Test Count | Purpose |
|----------|------------|------------|---------|
| **Password Validation** | `PasswordStrengthValidationTest` | 25 | Password security strength evaluation |
| **User Authentication** | `UserAuthenticationIntegrationTest` | 30 | Authentication workflow and session management |
| **Access Control** | `RoleBasedAccessControlTest` | 35 | Role-based permission system validation |
| **Question Submission** | `QuestionSubmissionValidationTest` | 25 | Question submission and validation testing |
| **Database Operations** | `DatabaseCRUDOperationsTest` | 25 | CRUD operations and data integrity |
| | | **140 Total** | **Comprehensive system validation** |

### Testing Technologies

- **JUnit 5**: Primary testing framework
- **Mockito**: Mocking and stubbing
- **AssertJ**: Fluent assertions
- **H2 Database**: In-memory testing database
- **Gradle**: Build and dependency management

## 📚 Documentation

### Generated Documentation

1. **Javadoc API Documentation**: [`docs/javadoc/index.html`](docs/javadoc/index.html)
   - Complete API documentation for all source classes
   - Professional formatting with cross-references
   - Method signatures, parameters, and return types

2. **Test Suite Documentation**: [`docs/TEST_DOCUMENTATION.md`](docs/TEST_DOCUMENTATION.md)
   - Detailed test strategy and methodology
   - Test case descriptions and expected outcomes
   - Framework architecture and design decisions

3. **Execution Guide**: [`docs/TEST_EXECUTION_EVIDENCE.md`](docs/TEST_EXECUTION_EVIDENCE.md)
   - Java 24 compatibility solutions
   - Step-by-step execution instructions
   - Troubleshooting guide for common issues

## ☕ Java 24 Compatibility

### Required Configuration

This project uses Java 24 which requires specific configuration for the testing frameworks:

#### Gradle Version
```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.11-bin.zip
```

#### JVM Arguments
```gradle
test {
    jvmArgs = [
        '--add-opens=java.base/java.lang=ALL-UNNAMED',
        '--add-opens=java.base/java.lang.reflect=ALL-UNNAMED',
        '--enable-preview'
    ]
}
```

### Why These Settings Are Needed

- **Gradle 8.11+**: Required for Java 24 support
- **JVM Arguments**: ByteBuddy (used by Mockito) needs access to internal JVM classes
- **Module Opens**: Required for reflection-based mocking in Java 24

## 🚀 Quick Start

### Prerequisites
- Java 24 JDK installed
- JAVA_HOME environment variable set
- Git for cloning the repository

### Run Tests
```bash
# Clone and navigate to project
git clone <repository-url>
cd HW3

# Verify Java version
java -version

# Run all tests
./gradlew test

# Generate reports
./gradlew test jacocoTestReport
```

### View Results
- **Test Report**: `build/reports/tests/test/index.html`
- **Coverage Report**: `build/reports/jacoco/test/html/index.html`
- **API Documentation**: `docs/javadoc/index.html`

## 🔧 Detailed Setup

### 1. Environment Verification
```bash
# Check Java version (should be 24)
java -version

# Check Gradle version (should be 8.11+)
./gradlew --version

# Verify JAVA_HOME
echo $JAVA_HOME  # Unix/Linux/Mac
echo %JAVA_HOME%  # Windows
```

### 2. Dependency Download
```bash
# Download all dependencies
./gradlew build --refresh-dependencies
```

### 3. Compilation Check
```bash
# Compile source code
./gradlew compileJava

# Compile test code
./gradlew compileTestJava
```

## 🧪 Test Execution

### Run Specific Test Categories
```bash
# Password validation tests
./gradlew test --tests "*PasswordStrengthValidationTest"

# Authentication tests
./gradlew test --tests "*UserAuthenticationIntegrationTest"

# Access control tests
./gradlew test --tests "*RoleBasedAccessControlTest"

# Question submission tests
./gradlew test --tests "*QuestionSubmissionValidationTest"

# Database operation tests
./gradlew test --tests "*DatabaseCRUDOperationsTest"
```

### Test Execution Options
```bash
# Run tests with detailed output
./gradlew test --info

# Run tests continuously (watch mode)
./gradlew test --continuous

# Run tests with debugging
./gradlew test --debug-jvm
```

## 📊 Quality Metrics

### Expected Test Results
- **Total Tests**: 140
- **Success Rate**: 100%
- **Execution Time**: <30 seconds
- **Code Coverage**: 90%+

### Quality Standards
- ✅ All tests have descriptive names
- ✅ Comprehensive Javadoc documentation
- ✅ Proper test isolation and cleanup
- ✅ Parameterized testing for data variations
- ✅ Mock usage for external dependencies

## 🔧 Troubleshooting

### Common Issues and Solutions

#### Issue: "Unsupported class file major version 68"
**Cause**: Using Gradle version that doesn't support Java 24  
**Solution**: Ensure Gradle 8.11+ is being used
```bash
./gradlew wrapper --gradle-version 8.11
```

#### Issue: "Unable to make protected final java.lang.Class"
**Cause**: ByteBuddy compatibility with Java 24  
**Solution**: Add JVM arguments to test configuration
```gradle
test {
    jvmArgs = ['--add-opens=java.base/java.lang=ALL-UNNAMED']
}
```

#### Issue: Tests not found or not executing
**Cause**: Compilation issues or path problems  
**Solution**: Clean and rebuild
```bash
./gradlew clean build
```

#### Issue: Mock creation failures
**Cause**: Module system restrictions  
**Solution**: Verify all JVM arguments are applied and ByteBuddy is compatible

### Getting Help

1. **Documentation**: Check [`docs/TEST_EXECUTION_EVIDENCE.md`](docs/TEST_EXECUTION_EVIDENCE.md)
2. **API Reference**: Browse [`docs/javadoc/index.html`](docs/javadoc/index.html)
3. **Test Details**: Review [`docs/TEST_DOCUMENTATION.md`](docs/TEST_DOCUMENTATION.md)

## 🎥 Demonstration

A comprehensive screencast demonstrating the project is available, covering:
- Code structure and organization
- Test implementation and design
- Documentation generation
- Test execution and results
- Java 24 compatibility solutions

## 🏆 Achievement Summary

This project demonstrates:
- **Professional Testing Practices**: 140 comprehensive test cases
- **Modern Java Development**: Java 24 compatibility and best practices
- **Documentation Excellence**: Complete API and test documentation
- **Quality Assurance**: High code coverage and thorough validation
- **Problem-Solving Skills**: Java 24 compatibility solutions

## 📝 Submission Notes

This implementation represents a complete automated testing system suitable for professional software development environments. All deliverables include comprehensive documentation, proper error handling, and industry-standard testing practices.

---

**Course**: CSE 360 - Software Engineering  
**Assignment**: HW3 - Automated Testing  
**Java Version**: 24  
**Build Tool**: Gradle 8.11  
**Test Framework**: JUnit 5 with Mockito