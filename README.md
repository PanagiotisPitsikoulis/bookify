# Travel Agency Booking System

A Java application for managing a travel agency's bookings, customers, and destinations. This system provides real-time updates on available travel options, booking confirmations, and generates detailed reports on customer travel history.

## Features

- **Customer Management**: Add, edit, and delete customer records
- **Destination Management**: Maintain a database of travel destinations with descriptions and pricing
- **Booking Management**: Create and manage travel bookings for customers
- **Real-time Updates**: Track booking status changes (Pending, Confirmed, Completed, Cancelled)
- **Detailed Reports**: Generate various reports including:
  - Customer travel history
  - Popular destinations
  - Booking summaries
  - Revenue reports
- **Data Export**: Export reports to CSV format for further analysis

## Admin Access

The system includes an admin dashboard for managing the application. To access the admin area, use the following credentials:

- **Username**: admin
- **Password**: admin123

Alternatively, you can use manager credentials:
- **Username**: manager
- **Password**: manager123

## Technical Specifications

- **Programming Language**: Java
- **UI Framework**: JavaFX
- **Database**: SQLite
- **Packaging**: Maven

## Running the Application

### Prerequisites

- Java JDK 11 or higher
- Maven

### Running on macOS (Apple Silicon)

```bash
./run-mac.sh
```

### Running on Other Platforms

```bash
mvn clean javafx:run
```

### Building a JAR File

```bash
./package-app.sh
```

## Screenshots

(Screenshots will be added here)

## Database Schema

The application uses an SQLite database with the following tables:

- **customers**: Stores customer information
- **destinations**: Stores destination details including pricing
- **bookings**: Manages the relationship between customers and destinations

## Implementation Notes

- All data is stored permanently in an SQLite database file
- The application follows an MVC architecture
- DAOs (Data Access Objects) are used to handle database operations

## Project Structure

- **src/main/java/com/bookify/app**: Main application code
  - **controller/**: UI controllers
  - **dao/**: Data Access Objects
  - **database/**: Database connection code
  - **model/**: Data models
- **src/main/resources/**: UI resources (FXML files)

## Future Enhancements

- Backup and restore functionality
- User authentication and permission levels
- Integration with external booking systems
- Mobile application

## Known Issues

### JavaFX Rendering Issue on macOS (Apple Silicon)

There's a known issue with JavaFX applications on macOS running on Apple Silicon processors related to NSTrackingRectTag exceptions. The exact error message is:

```
NSInternalInconsistencyException: 0x0 is an invalid NSTrackingRectTag. Common possible reasons for this are: 
1. already removed this trackingRectTag
2. Truncated the NSTrackingRectTag to 32bit at some point
```

This error causes the application to terminate unexpectedly when trying to display any JavaFX UI components.

### Possible Solutions

1. **Use a different JDK version**
   - Try using JDK 11 with JavaFX bundled
   - Try using JDK 18 or 19

2. **Use a different JavaFX version**
   - Try downgrading to JavaFX 16 or upgrading to newer versions

3. **Run the application on a different platform**
   - The application should work correctly on Windows or Linux

4. **Add JVM arguments to bypass the issue**
   - Try adding `--add-opens javafx.graphics/com.sun.glass.ui=ALL-UNNAMED` to the JVM arguments

### Running on Other Platforms

To run the application on Windows or Linux:
1. Clone the repository
2. Use the Maven command: `mvn clean javafx:run`

## Database Setup

The application automatically creates and initializes the SQLite database on first run. No additional setup is required.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 