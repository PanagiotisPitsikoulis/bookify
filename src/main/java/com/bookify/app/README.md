# Application Core

This directory contains the core application code for the Travel Agency Booking System.

## Package Structure

- **controller/**: UI controllers handling user interactions
- **dao/**: Data Access Objects for database operations
- **database/**: Database connection and utility classes
- **model/**: Data model classes
- **MainApp.java**: Application entry point

## MVC Architecture

The application follows the Model-View-Controller (MVC) pattern:

- **Model**: Data objects representing business entities
- **View**: FXML files defining the user interface
- **Controller**: Classes that handle UI events and coordinate with models

## Design Rationale

The MVC architecture separates concerns, making the code more maintainable and easier to test. This pattern is widely used in industry for UI applications.

> **Note**: The MVC pattern requires more initial setup but provides significant benefits for application maintenance and scalability.
