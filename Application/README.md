# TP2 - Run Instructions

This project uses Java 24 and JavaFX 24. To run the application easily, use Gradle which will pull JavaFX and H2 for you.

Run with system Gradle (or add a Gradle wrapper):

PowerShell:

```
gradle runApp
```

If you prefer not to use Gradle, you can run with the manual commands I used (from the project root):

PowerShell (compile):

```
javac --module-path .\javafx-sdk\javafx-sdk-24.0.2\lib --add-modules javafx.controls,javafx.fxml -d bin @(Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
```

PowerShell (run):

```
java --module-path .\javafx-sdk\javafx-sdk-24.0.2\lib --add-modules javafx.controls,javafx.fxml -cp ".\bin;.\n+lib\h2-2.2.224.jar" application.StartCSE360
```

Notes:
- I added a minimal `StudentHomePage.java` to get the project to compile.
- `module-info.java` was temporarily renamed to `module-info.java.bak` during my run. If you want to restore modular execution, I can help re-enable it.
