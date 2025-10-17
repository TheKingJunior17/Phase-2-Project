<#
Simple runner for the project (PowerShell).
Usage: In PowerShell, from project root run: .\run.ps1
This compiles sources under src into bin and runs the main class application.StartCSE360
It expects the OpenJFX SDK to be at .\javafx-sdk\javafx-sdk-24.0.2 and H2 jar at .\lib\h2-2.2.224.jar (both are present from previous steps).
#>

Set-StrictMode -Version Latest

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $projectRoot

if (-not (Test-Path .\javafx-sdk\javafx-sdk-24.0.2\lib)) {
    Write-Error "JavaFX SDK not found under .\javafx-sdk\javafx-sdk-24.0.2\lib. Please download OpenJFX 24 and place it there, or use Gradle runApp."
    exit 1
}

if (-not (Test-Path .\lib\h2-2.2.224.jar)) {
    Write-Error "H2 JDBC jar not found under .\lib\h2-2.2.224.jar. Please run the helper to download it or use Gradle."
    exit 1
}

if (-not (Test-Path bin)) { New-Item -ItemType Directory -Path bin | Out-Null }

Write-Host "Compiling Java sources..." -ForegroundColor Cyan
javac --module-path .\javafx-sdk\javafx-sdk-24.0.2\lib --add-modules javafx.controls,javafx.fxml -d bin @(Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed. See messages above."; Pop-Location; exit $LASTEXITCODE
}

Write-Host "Running application... (JVM Xmx=512m)" -ForegroundColor Cyan
java -Xmx512m --enable-native-access=javafx.graphics --module-path .\javafx-sdk\javafx-sdk-24.0.2\lib --add-modules javafx.controls,javafx.fxml -cp ".\bin;.\lib\h2-2.2.224.jar" application.StartCSE360

Pop-Location
