@echo off
echo ======================================================================
echo 🎓 TP3 Enhanced Application - Phase 3 Demo
echo    CSE 360 - Introduction to Software Engineering  
echo    Student: Jose Mendoza
echo ======================================================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set JAVAFX_PATH=..\Application\javafx-sdk\javafx-sdk-24.0.2\lib
set SRC_PATH=src\main\java
set BUILD_PATH=build\classes\java\main

echo 🔧 Attempting to run with JavaFX GUI...

if exist "%JAVAFX_PATH%" (
    echo ✅ JavaFX found - compiling GUI version...
    
    "%JAVA_HOME%\bin\javac.exe" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "%SRC_PATH%" -d "%BUILD_PATH%" "%SRC_PATH%\edu\asu\cse360\hw3\TP3JavaFXApp.java" 2>nul
    
    if errorlevel 0 (
        echo ✅ JavaFX compilation successful!
        echo 🚀 Starting JavaFX GUI Application...
        
        "%JAVA_HOME%\bin\java.exe" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml -cp "%BUILD_PATH%" edu.asu.cse360.hw3.TP3JavaFXApp
        
        if errorlevel 0 (
            echo 🎉 JavaFX Application completed successfully!
            goto end
        )
    )
)

echo 🔄 JavaFX failed or not available - running console version...
echo.

"%JAVA_HOME%\bin\javac.exe" -cp "%SRC_PATH%" -d "%BUILD_PATH%" "%SRC_PATH%\edu\asu\cse360\hw3\TP3AppRunner.java" >nul 2>&1

if errorlevel 1 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo ✅ Console version compiled successfully!
echo 🎮 Starting TP3 Application Demo...
echo.

"%JAVA_HOME%\bin\java.exe" -cp "%BUILD_PATH%" edu.asu.cse360.hw3.TP3AppRunner

:end
echo.
echo ======================================================================
echo 🎉 TP3 Application Demo Completed!
echo ======================================================================
pause