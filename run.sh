#!/bin/bash
# Travel Agency Booking System - Run Script for Linux/macOS

echo "===================================================="
echo "     Travel Agency Booking System - Launcher"
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

echo "Starting Travel Agency Booking System..."
echo

# Check for macOS
if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Detected macOS environment."
    
    # Check for Apple Silicon
    if [[ $(uname -m) == 'arm64' ]]; then
        echo "Detected Apple Silicon (M1/M2) processor."
        
        # Check for Silicon fix parameter
        if [[ "$1" == "--silicon-fix" ]]; then
            echo "Applying Apple Silicon fix..."
            mvn clean javafx:run -Djavafx.options="--add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED"
            exit $?
        fi
        
        echo "Note: If you experience issues, try running with './run.sh --silicon-fix'"
    fi
fi

# Run the application using Maven
mvn clean javafx:run

echo
echo "Application closed." 