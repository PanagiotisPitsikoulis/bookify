@echo off
REM Travel Agency Booking System - Run Script for Windows

echo ====================================================
echo      Travel Agency Booking System - Launcher
echo ====================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in the PATH.
    echo Please install Java 11 or higher and try again.
    goto :end
)

REM Check for Maven
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in the PATH.
    echo Please install Maven 3.6+ and try again.
    goto :end
)

echo Starting Travel Agency Booking System...
echo.

REM Check for Apple Silicon fix parameter
if "%1"=="--silicon-fix" (
    echo Running with Apple Silicon fix...
    mvn clean javafx:run -Djavafx.options="--add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED"
) else (
    REM Run the application using Maven
    mvn clean javafx:run
)

echo.
echo Application closed.

:end
pause 