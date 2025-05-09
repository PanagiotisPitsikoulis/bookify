package com.bookify.app.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bookify.app.MainApp;
import com.bookify.app.dao.BookingDAO;
import com.bookify.app.dao.CustomerDAO;
import com.bookify.app.dao.DestinationDAO;
import com.bookify.app.model.Booking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the admin dashboard
 */
public class AdminController {

    @FXML
    private Label userLabel;
    @FXML
    private VBox contentPane;
    @FXML
    private TextField searchField;

    // Statistics labels
    @FXML
    private Label customerCountLabel;
    @FXML
    private Label destinationCountLabel;
    @FXML
    private Label bookingCountLabel;

    // Recent bookings table
    @FXML
    private TableView<Booking> recentBookingsTable;
    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> bookingCustomerColumn;
    @FXML
    private TableColumn<Booking, String> bookingDestinationColumn;
    @FXML
    private TableColumn<Booking, String> bookingDateColumn;
    @FXML
    private TableColumn<Booking, String> bookingStatusColumn;

    // Charts
    @FXML
    private PieChart statusChart;
    @FXML
    private BarChart<String, Number> revenueChart;

    // Menu buttons
    @FXML
    private Button dashboardButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button systemButton;
    @FXML
    private Button backupButton;
    @FXML
    private Button reportsButton;

    // DAOs
    private CustomerDAO customerDAO;
    private DestinationDAO destinationDAO;
    private BookingDAO bookingDAO;

    // Current logged in user
    private String currentUser;

    @FXML
    public void initialize() {
        // Initialize DAOs
        customerDAO = new CustomerDAO();
        destinationDAO = new DestinationDAO();
        bookingDAO = new BookingDAO();

        // Set up table columns
        setupTableColumns();

        // Load data
        loadDashboardData();

        // Set active button
        setActiveButton(dashboardButton);
    }

    private void setupTableColumns() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        bookingDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationName"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("travelDate"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDashboardData() {
        try {
            // Load statistics
            int customerCount = customerDAO.getAllCustomers().size();
            int destinationCount = destinationDAO.getAllDestinations().size();
            List<Booking> allBookings = bookingDAO.getAllBookings();
            int bookingCount = allBookings.size();

            // Update labels
            customerCountLabel.setText(String.valueOf(customerCount));
            destinationCountLabel.setText(String.valueOf(destinationCount));
            bookingCountLabel.setText(String.valueOf(bookingCount));

            // Load recent bookings
            ObservableList<Booking> recentBookings = FXCollections.observableArrayList(
                    allBookings.stream()
                            .sorted((b1, b2) -> b2.getTravelDate().compareTo(b1.getTravelDate()))
                            .limit(10)
                            .collect(Collectors.toList()));
            recentBookingsTable.setItems(recentBookings);

            // Create status distribution chart
            createStatusChart(allBookings);

            // Create revenue chart
            createRevenueChart(allBookings);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Data Loading Error",
                    "An error occurred while loading dashboard data: " + e.getMessage());
        }
    }

    private void createStatusChart(List<Booking> bookings) {
        // Count bookings by status
        Map<String, Integer> statusCounts = new HashMap<>();
        for (Booking booking : bookings) {
            String status = booking.getStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statusCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // Set chart data
        statusChart.setData(pieChartData);
    }

    private void createRevenueChart(List<Booking> bookings) {
        // Calculate revenue by destination
        Map<String, Double> revenueByDestination = new HashMap<>();
        for (Booking booking : bookings) {
            String destination = booking.getDestinationName();
            Double currentRevenue = revenueByDestination.getOrDefault(destination, 0.0);
            revenueByDestination.put(destination, currentRevenue + booking.getTotalPrice());
        }

        // Create chart series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        // Add data to series
        for (Map.Entry<String, Double> entry : revenueByDestination.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Clear previous data and add new series
        revenueChart.getData().clear();
        revenueChart.getData().add(series);
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
        userLabel.setText("Logged in as: " + username);
    }

    @FXML
    void showDashboard(ActionEvent event) {
        setActiveButton(dashboardButton);
        loadDashboardData();
    }

    @FXML
    void showUsers(ActionEvent event) {
        setActiveButton(usersButton);
        showAlert(Alert.AlertType.INFORMATION, "Users", "User Management",
                "This would display the user management interface.");
    }

    @FXML
    void showSystem(ActionEvent event) {
        setActiveButton(systemButton);
        showAlert(Alert.AlertType.INFORMATION, "System", "System Settings",
                "This would display the system settings interface.");
    }

    @FXML
    void showBackup(ActionEvent event) {
        setActiveButton(backupButton);
        showAlert(Alert.AlertType.INFORMATION, "Backup", "Backup & Restore",
                "This would display the backup and restore interface.");
    }

    @FXML
    void showReports(ActionEvent event) {
        setActiveButton(reportsButton);
        showAlert(Alert.AlertType.INFORMATION, "Reports", "Reports Management",
                "This would display the reports management interface.");
    }

    @FXML
    void performSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search", "Empty Search",
                    "Please enter a search term.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Search", "Search Results",
                "This would display search results for: " + searchTerm);
    }

    @FXML
    void generateReport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write report header
                writer.write("Travel Agency Admin Report\n\n");

                // Write statistics
                writer.write("Statistics:\n");
                writer.write("Total Customers," + customerCountLabel.getText() + "\n");
                writer.write("Total Destinations," + destinationCountLabel.getText() + "\n");
                writer.write("Total Bookings," + bookingCountLabel.getText() + "\n\n");

                // Write bookings
                writer.write("Recent Bookings:\n");
                writer.write("ID,Customer,Destination,Travel Date,Status\n");

                for (Booking booking : recentBookingsTable.getItems()) {
                    writer.write(booking.getId() + "," +
                            booking.getCustomerName() + "," +
                            booking.getDestinationName() + "," +
                            booking.getTravelDate() + "," +
                            booking.getStatus() + "\n");
                }

                showAlert(Alert.AlertType.INFORMATION, "Report", "Report Generated",
                        "Report has been saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Report Generation Error",
                        "Error generating report: " + e.getMessage());
            }
        }
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            // Load login screen
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/login.fxml"));
            Parent loginView = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the login scene
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.setTitle("Travel Agency Admin Login");
            stage.setMaximized(false);
            stage.setWidth(600);
            stage.setHeight(400);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Logout Error",
                    "Error during logout: " + e.getMessage());
        }
    }

    @FXML
    void returnToMainApp(ActionEvent event) {
        try {
            // Load main application view
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/main.fxml"));
            Parent mainView = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the main scene
            Scene scene = new Scene(mainView);
            stage.setScene(scene);
            stage.setTitle("Travel Agency Booking System");
            stage.setMaximized(false);
            stage.setWidth(900);
            stage.setHeight(600);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error",
                    "Error returning to main application: " + e.getMessage());
        }
    }

    private void setActiveButton(Button button) {
        // Remove active class from all buttons
        dashboardButton.getStyleClass().remove("active");
        usersButton.getStyleClass().remove("active");
        systemButton.getStyleClass().remove("active");
        backupButton.getStyleClass().remove("active");
        reportsButton.getStyleClass().remove("active");

        // Add active class to selected button
        button.getStyleClass().add("active");
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Methods for User Management tab
    @FXML
    void searchUsers(ActionEvent event) {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search", "Empty Search",
                    "Please enter a search term.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Search", "User Search",
                "This would search for users matching: " + searchTerm);
    }

    @FXML
    void showAddUserDialog(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Users", "Add User",
                "This would display a dialog to add a new user.");
    }

    // Methods for System Settings tab
    @FXML
    void saveSettings(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Settings", "Save Settings",
                "This would save the system settings.");
    }

    @FXML
    void resetSettings(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Settings", "Reset Settings",
                "This would reset the system settings to defaults.");
    }

    // Methods for Database tab
    @FXML
    void backupDatabase(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Database", "Backup Database",
                "This would backup the database to a file.");
    }

    @FXML
    void restoreDatabase(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Database", "Restore Database",
                "This would restore the database from a backup file.");
    }
}