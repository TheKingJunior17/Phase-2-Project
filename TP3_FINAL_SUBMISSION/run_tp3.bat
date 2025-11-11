@echo off
echo ===============================================
echo TP3 Enhanced Application - Build and Run Script
echo CSE 360 - Team Project Phase 3
echo ===============================================
echo.

echo [1/3] Creating build directory...
if not exist "build" mkdir build

echo [2/3] Compiling Java source files...
javac -d build src\*.java
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo [3/3] Running TP3 Application...
echo.
java -cp build TP3AppRunner

echo.
echo ===============================================
echo Application completed successfully!
echo ===============================================
pause