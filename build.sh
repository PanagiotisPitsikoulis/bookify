#!/bin/bash
# Travel Agency Booking System - Build Script for Linux/macOS

echo "===================================================="
echo "    Travel Agency Booking System - Build Tool"
echo "===================================================="
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in the PATH."
    echo "Please install Java 11 or higher and try again."
    exit 1
fi

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in the PATH."
    echo "Please install Maven 3.6+ and try again."
    exit 1
fi

echo "Building Travel Agency Booking System..."
echo

# Clean and package the application
mvn clean package

if [ $? -ne 0 ]; then
    echo
    echo "Build failed! See above for details."
    exit 1
else
    echo
    echo "Build successful!"
    echo "The JAR file is located at: target/travel-agency-1.0-SNAPSHOT.jar"
    echo
    echo "To run the application, use: java -jar target/travel-agency-1.0-SNAPSHOT.jar"
fi 