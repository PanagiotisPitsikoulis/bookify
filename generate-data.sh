#!/bin/bash
# Script to generate sample data for the Bookify Travel Agency application

echo "======================================================"
echo "     Bookify Travel Agency - Data Generator"
echo "======================================================"
echo

mvn exec:java -Dexec.mainClass="com.bookify.app.MainApp" -Dexec.args="--generate-data"

echo
echo "Sample data generation process completed."
echo "======================================================" 