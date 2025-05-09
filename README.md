# 🌍 Travel Agency Booking System

A JavaFX application developed as a class project. This booking system for travel agencies allows managing customers, destinations, and bookings.

# More Projects

https://www.panagiotispitsikoulis.gr/posts?category=Project&sort=date-desc&layout=grid

# Project Architecture

Model View Controller Architecture

## ✨ Features

- **👥 Customer Management**: Add, edit, and delete customer information
- **🏝️ Destinations**: Manage travel destinations with descriptions
- **📝 Bookings**: Create and manage customer bookings
- **🔄 Status Updates**: Track booking status (Pending, Confirmed, etc.)
- **📊 Reports**: Generate visual data reports and statistics
- **📤 Export Options**: Export data to CSV format
- **🔒 Admin Panel**: Secure administrative interface

## 🚀 Running the Application

### Dependencies

- Java JDK 11+ (Tested with JDK 17)
- Maven for dependency management

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/PanagiotisPitsikoulis/bookify
   cd bookify
   ```

2. Launch the application:

   **Mac/Linux**:

   ```bash
   # Make the script executable
   chmod +x run.sh

   # Start the application
   ./run.sh
   ```

   **Windows**:

   ```
   run.bat
   ```

### Admin Login

Admin credentials:

- **Username**: admin
- **Password**: admin123

## 🛠️ Building from Source

### Creating a JAR File

**Mac/Linux**:

```bash
./build.sh
```

**Windows**:

```
build.bat
```

The compiled JAR file will be located in the `target` directory.

## 📚 Project Structure

```
bookify/
├── src/                    # Source code
│   ├── main/
│       ├── java/          # Java source files
│       │   └── com/
│       │       └── bookify/
│       │           └── app/
│       │               ├── controller/  # UI controllers
│       │               ├── dao/         # Data access objects
│       │               ├── database/    # Database connection
│       │               └── model/       # Data models
│       └── resources/     # UI resources
│           ├── css/       # Stylesheets
│           └── fxml/      # Interface layouts
├── target/                # Compiled files
├── run.sh                 # Mac/Linux script
├── run.bat                # Windows script
├── build.sh               # Mac/Linux build
├── build.bat              # Windows build
└── pom.xml                # Maven configuration
```

## 💾 Database

The application uses SQLite with three main tables:

- **customers**: Customer information
- **destinations**: Travel destination details
- **bookings**: Reservation records

## 🐞 Known Issues

For Mac users with Apple Silicon (M1/M2/M3/M4) experiencing crashes:

```bash
./run.sh --silicon-fix
```

## 👨‍💻 Author

Panagiotis Pitsikoulis
https://panagiotispitsikoulis.gr
