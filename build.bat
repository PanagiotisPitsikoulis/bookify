@echo off
REM Travel Agency Booking System - Build Script for Windows

echo ====================================================
echo     Travel Agency Booking System - Build Tool
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

echo Building Travel Agency Booking System...
echo.

REM Clean and package the application
mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Build failed! See above for details.
) else (
    echo.
    echo Build successful!
    echo The JAR file is located at: target\travel-agency-1.0-SNAPSHOT.jar
    echo.
    echo To run the application, use: java -jar target\travel-agency-1.0-SNAPSHOT.jar
)

:end
pause 