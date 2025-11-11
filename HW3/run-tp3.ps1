# TP3 Enhanced Application Runner
Write-Host "======================================================================"
Write-Host "🎓 TP3 Enhanced Application - Phase 3 Demo"
Write-Host "   CSE 360 - Introduction to Software Engineering"
Write-Host "   Student: Jose Mendoza"
Write-Host "======================================================================"
Write-Host ""

$JAVA_HOME = "C:\Program Files\Java\jdk-24"
$JAVAFX_PATH = "..\Application\javafx-sdk\javafx-sdk-24.0.2\lib"
$SRC_PATH = "src\main\java"
$BUILD_PATH = "build\classes\java\main"

Write-Host "🔧 Checking for JavaFX..." 

if (Test-Path $JAVAFX_PATH) {
    Write-Host "✅ JavaFX found - attempting GUI version..."
    
    # Try JavaFX compilation
    Write-Host "📦 Compiling JavaFX version..."
    
    & "$JAVA_HOME\bin\javac.exe" --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml -cp "$SRC_PATH" -d "$BUILD_PATH" "$SRC_PATH\edu\asu\cse360\hw3\TP3JavaFXApp.java" 2>$null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ JavaFX compilation successful!"
        Write-Host "🚀 Starting JavaFX GUI Application..."
        
        & "$JAVA_HOME\bin\java.exe" --module-path "$JAVAFX_PATH" --add-modules javafx.controls,javafx.fxml -cp "$BUILD_PATH" edu.asu.cse360.hw3.TP3JavaFXApp
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "🎉 JavaFX Application completed!"
            exit 0
        }
    }
    
    Write-Host "⚠️ JavaFX version failed - using console version..."
} else {
    Write-Host "❌ JavaFX not found - using console version..."
}

Write-Host ""
Write-Host "🔄 Running TP3 Console Application..."
Write-Host ""

& "$JAVA_HOME\bin\javac.exe" -cp "$SRC_PATH" -d "$BUILD_PATH" "$SRC_PATH\edu\asu\cse360\hw3\TP3AppRunner.java"

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Console version compiled!"
    Write-Host "🎮 Starting TP3 Application Demo..."
    Write-Host ""
    
    & "$JAVA_HOME\bin\java.exe" -cp "$BUILD_PATH" edu.asu.cse360.hw3.TP3AppRunner
} else {
    Write-Host "❌ Compilation failed!"
    exit 1
}

Write-Host ""
Write-Host "======================================================================"
Write-Host "🎉 TP3 Application Demo Completed!"
Write-Host "======================================================================"