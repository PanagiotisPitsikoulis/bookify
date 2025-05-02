#!/bin/bash

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    exit 1
fi

# Compile and run the application
echo "Building and running Travel Agency Booking System..."
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED -Xmx1024m"
mvn clean javafx:run

# Exit
exit 0 