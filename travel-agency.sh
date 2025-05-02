#!/bin/bash

# Set JAVA_HOME to point to Zulu JDK 24
export JAVA_HOME=$(/usr/libexec/java_home -v 24)

# Go to the project directory
cd "$(dirname "$0")"

# Function to display usage
show_usage() {
    echo "Travel Agency Management System"
    echo "Usage: $0 [option]"
    echo "Options:"
    echo "  run          Run the application using Maven (default)"
    echo "  build        Build the executable JAR file"
    echo "  jar          Run the application from the JAR file"
    echo "  clean        Clean the project"
    echo "  help         Show this help message"
}

# Process command line arguments
case "$1" in
    run|"")
        # Run the application using Maven
        echo "Running Travel Agency Management System..."
        mvn clean javafx:run
        ;;
    build)
        # Build the executable JAR file
        echo "Building executable JAR file..."
        mvn clean package
        echo "JAR file created at: target/travel-agency-1.0-SNAPSHOT.jar"
        ;;
    jar)
        # Run the application from the JAR file
        echo "Running Travel Agency Management System from JAR file..."
        if [ ! -f "target/travel-agency-1.0-SNAPSHOT.jar" ]; then
            echo "JAR file not found. Building first..."
            mvn clean package
        fi
        java -jar target/travel-agency-1.0-SNAPSHOT.jar
        ;;
    clean)
        # Clean the project
        echo "Cleaning the project..."
        mvn clean
        ;;
    help)
        # Show help
        show_usage
        ;;
    *)
        # Invalid option
        echo "Invalid option: $1"
        show_usage
        exit 1
        ;;
esac 