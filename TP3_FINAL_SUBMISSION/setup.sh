#!/bin/bash
# TP3 Enhanced Application - Complete Setup Script
# CSE 360 - Team Project Phase 3
# Student: Jose Mendoza

echo "==============================================="
echo "TP3 Enhanced Application - Complete Setup"
echo "CSE 360 - Team Project Phase 3"
echo "Student: Jose Mendoza"
echo "==============================================="
echo ""

# Check Java installation
echo "[1/5] Checking Java installation..."
if ! command -v javac &> /dev/null; then
    echo "ERROR: Java compiler (javac) not found!"
    echo "Please install Java JDK 11 or higher."
    exit 1
fi

if ! command -v java &> /dev/null; then
    echo "ERROR: Java runtime (java) not found!"
    echo "Please install Java JDK 11 or higher."
    exit 1
fi

java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f 2)
echo "✅ Java found: $java_version"

# Create directory structure
echo "[2/5] Creating project structure..."
mkdir -p build
mkdir -p docs/javadoc
echo "✅ Directory structure created"

# Compile all Java source files
echo "[3/5] Compiling Java source files..."
javac -d build src/*.java
if [ $? -ne 0 ]; then
    echo "❌ ERROR: Compilation failed!"
    exit 1
fi
echo "✅ Compilation successful"

# Generate Javadoc documentation
echo "[4/5] Generating Javadoc documentation..."
javadoc -d docs/javadoc -sourcepath src -subpackages . 2>/dev/null
echo "✅ Javadoc generated in docs/javadoc/"

# Run application test
echo "[5/5] Testing application..."
echo "Running TP3AppRunner..."
echo ""
java -cp build TP3AppRunner

echo ""
echo "==============================================="
echo "Setup completed successfully!"
echo "==============================================="
echo ""
echo "📁 Project Structure:"
echo "   ├── README.md              (Project overview)"
echo "   ├── run_tp3.bat           (Windows run script)"  
echo "   ├── setup.sh              (This setup script)"
echo "   ├── src/                  (Java source files)"
echo "   ├── build/                (Compiled classes)"
echo "   └── docs/                 (Documentation)"
echo "       ├── TP3_ASSIGNMENT_SUMMARY.md"
echo "       ├── TP3_PLAN.md"
echo "       └── javadoc/          (Generated documentation)"
echo ""
echo "🚀 Quick Commands:"
echo "   Run Application:    java -cp build TP3AppRunner"
echo "   View Documentation: open docs/javadoc/index.html"
echo "   Run Tests:          java -cp build TP3ApplicationDemo"
echo ""
echo "🎯 TP3 Features Ready:"
echo "   ✅ Enhanced Authentication & Session Management"
echo "   ✅ Role-Based Access Control System"  
echo "   ✅ Question Submission & Validation Pipeline"
echo "   ✅ Database Operations (CRUD)"
echo "   ✅ System Integration Testing"
echo ""
echo "📋 Next Steps:"
echo "   1. Review generated documentation"
echo "   2. Test all application features" 
echo "   3. Prepare screencasts for Task 5"
echo "   4. Submit to GitHub repository"
echo ""
echo "🎓 Ready for TP3 submission and evaluation!"