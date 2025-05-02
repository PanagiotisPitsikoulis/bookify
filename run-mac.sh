#!/bin/bash

# Exit on error
set -e

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java JDK 11 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "Using Java version: $JAVA_VERSION"

echo "Building Travel Agency Booking System for macOS (Apple Silicon)..."

# Set environment variables for Apple Silicon
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED -Xmx1024m -Djava.library.path=/opt/homebrew/lib"

# Clean and build the project
echo "Building application..."
mvn clean package -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful. Starting application..."
    
    # Run with specific JVM options for macOS
    java --add-opens java.base/java.lang=ALL-UNNAMED \
         --enable-native-access=ALL-UNNAMED \
         -Djava.library.path=/opt/homebrew/lib \
         -Xmx1024m \
         -jar target/travel-agency-1.0-SNAPSHOT.jar
else
    echo "Build failed."
    exit 1
fi 