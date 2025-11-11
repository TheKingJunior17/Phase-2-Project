# TP3 JavaFX Application Runner
# This script compiles and runs the TP3 Enhanced Application with JavaFX GUI

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "🎓 TP3 Enhanced Application - JavaFX GUI Version" -ForegroundColor Yellow
Write-Host "   CSE 360 - Introduction to Software Engineering" -ForegroundColor Gray
Write-Host "   Team Project Phase 3 - GUI Application Demo" -ForegroundColor Gray
Write-Host "   Student: Jose Mendoza" -ForegroundColor Gray
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host ""

# Set paths
$JAVA_HOME = "C:\Program Files\Java\jdk-24"
$JAVAFX_PATH = ".\javafx-sdk\javafx-sdk-24.0.2\lib"
$SRC_PATH = "src\main\java"
$BUILD_PATH = "build\classes\java\main"

Write-Host "🔧 Checking JavaFX installation..." -ForegroundColor Blue

if (Test-Path $JAVAFX_PATH) {
    Write-Host "✅ JavaFX SDK found: $JAVAFX_PATH" -ForegroundColor Green
} else {
    Write-Host "❌ JavaFX SDK not found at: $JAVAFX_PATH" -ForegroundColor Red
    Write-Host "📥 Using Phase 2 Application JavaFX installation..." -ForegroundColor Yellow
    $JAVAFX_PATH = "..\Application\javafx-sdk\javafx-sdk-24.0.2\lib"
    
    if (Test-Path $JAVAFX_PATH) {
        Write-Host "✅ Found JavaFX in Phase 2 directory" -ForegroundColor Green
    } else {
        Write-Host "❌ JavaFX not found. Please install JavaFX SDK." -ForegroundColor Red
        Write-Host "🌐 Download from: https://gluonhq.com/products/javafx/" -ForegroundColor Yellow
        exit 1
    }
}

Write-Host ""
Write-Host "📦 Compiling TP3 JavaFX Application..." -ForegroundColor Blue

# Create build directory
if (!(Test-Path $BUILD_PATH)) {
    New-Item -ItemType Directory -Path $BUILD_PATH -Force | Out-Null
}

# Compile with JavaFX
$compileCommand = @"
"$JAVA_HOME\bin\javac.exe" --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml -cp "$SRC_PATH" -d "$BUILD_PATH" "$SRC_PATH\edu\asu\cse360\hw3\TP3JavaFXApp.java" "$SRC_PATH\edu\asu\cse360\hw3\*.java"
"@

Write-Host "Executing: $compileCommand" -ForegroundColor Gray
Invoke-Expression $compileCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilation successful!" -ForegroundColor Green
} else {
    Write-Host "❌ Compilation failed!" -ForegroundColor Red
    Write-Host "🔄 Trying alternative approach - using console version..." -ForegroundColor Yellow
    
    # Fallback to console version
    Write-Host ""
    Write-Host "🚀 Running TP3 Console Application instead..." -ForegroundColor Blue
    
    & "$JAVA_HOME\bin\javac.exe" -cp "$SRC_PATH" -d "$BUILD_PATH" "$SRC_PATH\edu\asu\cse360\hw3\TP3AppRunner.java"
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Console version compiled successfully!" -ForegroundColor Green
        Write-Host "🎮 Starting TP3 Application Demo..." -ForegroundColor Blue
        Write-Host ""
        
        & "$JAVA_HOME\bin\java.exe" -cp "$BUILD_PATH" edu.asu.cse360.hw3.TP3AppRunner
    } else {
        Write-Host "❌ Console version compilation failed!" -ForegroundColor Red
        exit 1
    }
    exit 0
}

Write-Host ""
Write-Host "🚀 Starting TP3 JavaFX Application..." -ForegroundColor Blue
Write-Host "🖥️  GUI window should open shortly..." -ForegroundColor Yellow

# Run JavaFX application
$runCommand = @"
"$JAVA_HOME\bin\java.exe" --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml -cp "$BUILD_PATH" edu.asu.cse360.hw3.TP3JavaFXApp
"@

Write-Host "Executing: $runCommand" -ForegroundColor Gray
Invoke-Expression $runCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "🎉 TP3 JavaFX Application completed successfully!" -ForegroundColor Green
    Write-Host "📝 All Phase 3 features demonstrated in GUI format." -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "⚠️  JavaFX application encountered issues." -ForegroundColor Yellow
    Write-Host "🔄 Running console fallback version..." -ForegroundColor Blue
    
    & "$JAVA_HOME\bin\java.exe" -cp "$BUILD_PATH" edu.asu.cse360.hw3.TP3AppRunner
}

Write-Host ""
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "🎓 Thank you for using the TP3 Enhanced Application!" -ForegroundColor Yellow
Write-Host "======================================================================" -ForegroundColor Cyan