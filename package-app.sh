#!/bin/bash

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven first."
    exit 1
fi

# Package the application
echo "Packaging Travel Agency Booking System..."
mvn clean package

echo ""
echo "Application packaged successfully!"
echo "To run the application, use: java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/travel-agency-1.0-SNAPSHOT.jar"

# Create a simpler run script
cat > run-jar.sh << 'EOF'
#!/bin/bash
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/travel-agency-1.0-SNAPSHOT.jar
EOF

chmod +x run-jar.sh

echo "Or simply run: ./run-jar.sh"

# Exit
exit 0 