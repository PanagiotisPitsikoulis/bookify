package com.bookify.app.controller;

import com.bookify.app.MainApp;
import com.bookify.app.dao.BookingDAO;
import com.bookify.app.dao.CustomerDAO;
import com.bookify.app.dao.DestinationDAO;
import com.bookify.app.model.Booking;
import com.bookify.app.model.Customer;
import com.bookify.app.model.Destination;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController {
    
    // DAOs
    private CustomerDAO customerDAO;
    private DestinationDAO destinationDAO;
    private BookingDAO bookingDAO;
    
    // Customer tab
    @FXML private TextField customerSearchField;
    @FXML private ListView<Customer> customerListView;
    @FXML private TextField customerNameField;
    @FXML private TextField customerEmailField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField customerAddressField;
    @FXML private TableView<Booking> customerBookingsTable;
    @FXML private TableColumn<Booking, Integer> customerBookingIdColumn;
    @FXML private TableColumn<Booking, String> customerBookingDestinationColumn;
    @FXML private TableColumn<Booking, String> customerBookingDateColumn;
    @FXML private TableColumn<Booking, String> customerBookingStatusColumn;
    
    // Destination tab
    @FXML private TextField destinationSearchField;
    @FXML private ListView<Destination> destinationListView;
    @FXML private TextField destinationNameField;
    @FXML private TextField destinationCountryField;
    @FXML private TextField destinationDescriptionField;
    @FXML private TextField destinationPriceField;
    
    // Booking tab
    @FXML private TextField bookingSearchField;
    @FXML private TableView<Booking> bookingsTable;
    @FXML private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML private TableColumn<Booking, String> bookingCustomerColumn;
    @FXML private TableColumn<Booking, String> bookingDestinationColumn;
    @FXML private TableColumn<Booking, String> bookingTravelDateColumn;
    @FXML private TableColumn<Booking, Integer> bookingPeopleColumn;
    @FXML private TableColumn<Booking, Double> bookingPriceColumn;
    @FXML private TableColumn<Booking, String> bookingStatusColumn;
    
    // Reports tab
    @FXML private Label reportTitleLabel;
    @FXML private TableView<Object> reportTable;
    
    // Admin tab
    @FXML private BorderPane adminContainer;
    @FXML private VBox adminLoginBox;
    @FXML private TextField adminUsernameField;
    @FXML private PasswordField adminPasswordField;
    @FXML private Label adminErrorLabel;
    
    // Status
    @FXML private Label statusLabel;
    
    // Data lists
    private ObservableList<Customer> customers;
    private ObservableList<Destination> destinations;
    private ObservableList<Booking> bookings;
    
    // Selected items
    private Customer selectedCustomer;
    private Destination selectedDestination;
    private Booking selectedBooking;
    
    // Admin authentication
    private boolean isAdminAuthenticated = false;
    
    @FXML
    public void initialize() {
        // Initialize DAOs
        customerDAO = new CustomerDAO();
        destinationDAO = new DestinationDAO();
        bookingDAO = new BookingDAO();
        
        // Initialize observable lists
        customers = FXCollections.observableArrayList();
        destinations = FXCollections.observableArrayList();
        bookings = FXCollections.observableArrayList();
        
        // Setup table columns
        setupTableColumns();
        
        // Setup listeners
        setupListeners();
        
        // Load initial data
        loadData();
        
        updateStatus("Application initialized successfully!");
    }
    
    private void setupTableColumns() {
        // Customer bookings table
        customerBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerBookingDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationName"));
        customerBookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("travelDate"));
        customerBookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // All bookings table
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        bookingDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationName"));
        bookingTravelDateColumn.setCellValueFactory(new PropertyValueFactory<>("travelDate"));
        bookingPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPeople"));
        bookingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    private void setupListeners() {
        // Customer selection changed
        customerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCustomer = newValue;
                displayCustomerDetails(newValue);
                loadCustomerBookings(newValue.getId());
            }
        });
        
        // Destination selection changed
        destinationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedDestination = newValue;
                displayDestinationDetails(newValue);
            }
        });
        
        // Booking selection changed
        bookingsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedBooking = newValue;
            }
        });
    }
    
    private void loadData() {
        try {
            // Load customers
            List<Customer> customerList = customerDAO.getAllCustomers();
            customers.setAll(customerList);
            customerListView.setItems(customers);
            updateStatus("Loaded " + customers.size() + " customers");
            
            // Load destinations
            List<Destination> destinationList = destinationDAO.getAllDestinations();
            destinations.setAll(destinationList);
            destinationListView.setItems(destinations);
            updateStatus("Loaded " + destinations.size() + " destinations");
            
            // Load bookings
            List<Booking> bookingList = bookingDAO.getAllBookings();
            bookings.setAll(bookingList);
            bookingsTable.setItems(bookings);
            updateStatus("Loaded " + bookings.size() + " bookings");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error Loading Data", 
                    "An error occurred while loading data: " + e.getMessage());
            updateStatus("Error loading data");
        }
    }
    
    private void displayCustomerDetails(Customer customer) {
        customerNameField.setText(customer.getName());
        customerEmailField.setText(customer.getEmail());
        customerPhoneField.setText(customer.getPhone());
        customerAddressField.setText(customer.getAddress());
    }
    
    private void displayDestinationDetails(Destination destination) {
        destinationNameField.setText(destination.getName());
        destinationCountryField.setText(destination.getCountry());
        destinationDescriptionField.setText(destination.getDescription());
        destinationPriceField.setText(String.valueOf(destination.getPricePerPerson()));
    }
    
    private void loadCustomerBookings(int customerId) {
        try {
            List<Booking> customerBookings = bookingDAO.getBookingsByCustomerId(customerId);
            customerBookingsTable.setItems(FXCollections.observableArrayList(customerBookings));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error Loading Customer Bookings", 
                    "An error occurred while loading customer bookings: " + e.getMessage());
        }
    }
    
    // Customer methods
    @FXML
    private void addCustomer() {
        resetCustomerForm();
        selectedCustomer = null;
        updateStatus("Adding new customer");
    }
    
    @FXML
    private void editCustomer() {
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Customer Selected", 
                    "Please select a customer to edit.");
            return;
        }
        updateStatus("Editing customer: " + selectedCustomer.getName());
    }
    
    @FXML
    private void deleteCustomer() {
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Customer Selected", 
                    "Please select a customer to delete.");
            return;
        }
        
        boolean confirmed = showConfirmationDialog("Delete Customer", 
                "Are you sure you want to delete this customer?", 
                "This action cannot be undone.");
        
        if (confirmed) {
            try {
                customerDAO.deleteCustomer(selectedCustomer.getId());
                customers.remove(selectedCustomer);
                resetCustomerForm();
                selectedCustomer = null;
                updateStatus("Customer deleted successfully");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Customer", 
                        "An error occurred while deleting the customer: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void saveCustomer() {
        String name = customerNameField.getText();
        String email = customerEmailField.getText();
        String phone = customerPhoneField.getText();
        String address = customerAddressField.getText();
        
        if (name.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Input", 
                    "Name and email are required fields.");
            return;
        }
        
        try {
            if (selectedCustomer == null) {
                // Add new customer
                Customer newCustomer = new Customer();
                newCustomer.setName(name);
                newCustomer.setEmail(email);
                newCustomer.setPhone(phone);
                newCustomer.setAddress(address);
                
                boolean added = customerDAO.addCustomer(newCustomer);
                if (added) {
                    customers.add(newCustomer);
                    customerListView.getSelectionModel().select(newCustomer);
                    updateStatus("New customer added successfully");
                }
            } else {
                // Update existing customer
                selectedCustomer.setName(name);
                selectedCustomer.setEmail(email);
                selectedCustomer.setPhone(phone);
                selectedCustomer.setAddress(address);
                
                boolean updated = customerDAO.updateCustomer(selectedCustomer);
                if (updated) {
                    // Refresh list view
                    int index = customers.indexOf(selectedCustomer);
                    customers.set(index, selectedCustomer);
                    updateStatus("Customer updated successfully");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error Saving Customer", 
                    "An error occurred while saving the customer: " + e.getMessage());
        }
    }
    
    @FXML
    private void resetCustomerForm() {
        customerNameField.clear();
        customerEmailField.clear();
        customerPhoneField.clear();
        customerAddressField.clear();
        customerBookingsTable.getItems().clear();
    }
    
    // Destination methods
    @FXML
    private void addDestination() {
        resetDestinationForm();
        selectedDestination = null;
        updateStatus("Adding new destination");
    }
    
    @FXML
    private void editDestination() {
        if (selectedDestination == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Destination Selected", 
                    "Please select a destination to edit.");
            return;
        }
        updateStatus("Editing destination: " + selectedDestination.getName());
    }
    
    @FXML
    private void deleteDestination() {
        if (selectedDestination == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Destination Selected", 
                    "Please select a destination to delete.");
            return;
        }
        
        boolean confirmed = showConfirmationDialog("Delete Destination", 
                "Are you sure you want to delete this destination?", 
                "This action cannot be undone.");
        
        if (confirmed) {
            try {
                destinationDAO.deleteDestination(selectedDestination.getId());
                destinations.remove(selectedDestination);
                resetDestinationForm();
                selectedDestination = null;
                updateStatus("Destination deleted successfully");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Destination", 
                        "An error occurred while deleting the destination: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void saveDestination() {
        String name = destinationNameField.getText();
        String country = destinationCountryField.getText();
        String description = destinationDescriptionField.getText();
        String priceString = destinationPriceField.getText();
        
        if (name.isEmpty() || country.isEmpty() || priceString.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Input", 
                    "Name, country, and price are required fields.");
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid Price", 
                    "Price must be a valid number.");
            return;
        }
        
        try {
            if (selectedDestination == null) {
                // Add new destination
                Destination newDestination = new Destination();
                newDestination.setName(name);
                newDestination.setCountry(country);
                newDestination.setDescription(description);
                newDestination.setPricePerPerson(price);
                
                boolean added = destinationDAO.addDestination(newDestination);
                if (added) {
                    destinations.add(newDestination);
                    destinationListView.getSelectionModel().select(newDestination);
                    updateStatus("New destination added successfully");
                }
            } else {
                // Update existing destination
                selectedDestination.setName(name);
                selectedDestination.setCountry(country);
                selectedDestination.setDescription(description);
                selectedDestination.setPricePerPerson(price);
                
                boolean updated = destinationDAO.updateDestination(selectedDestination);
                if (updated) {
                    // Refresh list view
                    int index = destinations.indexOf(selectedDestination);
                    destinations.set(index, selectedDestination);
                    updateStatus("Destination updated successfully");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error Saving Destination", 
                    "An error occurred while saving the destination: " + e.getMessage());
        }
    }
    
    @FXML
    private void resetDestinationForm() {
        destinationNameField.clear();
        destinationCountryField.clear();
        destinationDescriptionField.clear();
        destinationPriceField.clear();
    }
    
    // Booking methods
    @FXML
    private void searchBookings() {
        String searchTerm = bookingSearchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            bookingsTable.setItems(bookings);
            return;
        }
        
        ObservableList<Booking> filteredList = FXCollections.observableArrayList();
        for (Booking booking : bookings) {
            if (booking.getCustomerName().toLowerCase().contains(searchTerm) ||
                booking.getDestinationName().toLowerCase().contains(searchTerm) ||
                booking.getStatus().toLowerCase().contains(searchTerm)) {
                filteredList.add(booking);
            }
        }
        
        bookingsTable.setItems(filteredList);
        updateStatus("Found " + filteredList.size() + " matching bookings");
    }
    
    @FXML
    private void newBooking() {
        // This would normally open a dialog to create a new booking
        showAlert(Alert.AlertType.INFORMATION, "Information", "New Booking", 
                "This feature will open a dialog to create a new booking.");
        updateStatus("Creating new booking");
    }
    
    @FXML
    private void editBooking() {
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Booking Selected", 
                    "Please select a booking to edit.");
            return;
        }
        // This would normally open a dialog to edit the booking
        showAlert(Alert.AlertType.INFORMATION, "Information", "Edit Booking", 
                "This feature will open a dialog to edit booking #" + selectedBooking.getId());
        updateStatus("Editing booking #" + selectedBooking.getId());
    }
    
    @FXML
    private void deleteBooking() {
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Booking Selected", 
                    "Please select a booking to delete.");
            return;
        }
        
        boolean confirmed = showConfirmationDialog("Delete Booking", 
                "Are you sure you want to delete this booking?", 
                "This action cannot be undone.");
        
        if (confirmed) {
            try {
                bookingDAO.deleteBooking(selectedBooking.getId());
                bookings.remove(selectedBooking);
                selectedBooking = null;
                updateStatus("Booking deleted successfully");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error Deleting Booking", 
                        "An error occurred while deleting the booking: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void changeBookingStatus() {
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Booking Selected", 
                    "Please select a booking to change its status.");
            return;
        }
        
        // Simple status change to "Confirmed" for demo
        selectedBooking.setStatus("Confirmed");
        try {
            bookingDAO.updateBooking(selectedBooking);
            
            // Refresh the table
            int index = bookings.indexOf(selectedBooking);
            bookings.set(index, selectedBooking);
            
            updateStatus("Booking status updated to: Confirmed");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error Updating Status", 
                    "An error occurred while updating the booking status: " + e.getMessage());
        }
    }
    
    // Report methods
    @FXML
    private void generateCustomerReport() {
        reportTitleLabel.setText("Customer Travel History Report");
        // Placeholder for actual report generation
        showAlert(Alert.AlertType.INFORMATION, "Report", "Customer Travel History", 
                "This feature will generate a detailed customer travel history report.");
        updateStatus("Generated customer travel history report");
    }
    
    @FXML
    private void generateDestinationReport() {
        reportTitleLabel.setText("Popular Destinations Report");
        // Placeholder for actual report generation
        showAlert(Alert.AlertType.INFORMATION, "Report", "Popular Destinations", 
                "This feature will generate a report of the most popular destinations.");
        updateStatus("Generated popular destinations report");
    }
    
    @FXML
    private void generateBookingReport() {
        reportTitleLabel.setText("Booking Summary Report");
        // Placeholder for actual report generation
        showAlert(Alert.AlertType.INFORMATION, "Report", "Booking Summary", 
                "This feature will generate a summary of all bookings.");
        updateStatus("Generated booking summary report");
    }
    
    @FXML
    private void generateRevenueReport() {
        reportTitleLabel.setText("Revenue Report");
        // Placeholder for actual report generation
        showAlert(Alert.AlertType.INFORMATION, "Report", "Revenue Report", 
                "This feature will generate a detailed revenue report.");
        updateStatus("Generated revenue report");
    }
    
    @FXML
    private void exportReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("Sample Report Data,Value\n");
                writer.write("Total Bookings," + bookings.size() + "\n");
                writer.write("Total Customers," + customers.size() + "\n");
                writer.write("Total Destinations," + destinations.size() + "\n");
                
                showAlert(Alert.AlertType.INFORMATION, "Export", "Report Exported", 
                        "The report has been exported to " + file.getAbsolutePath());
                updateStatus("Report exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Export Error", 
                        "An error occurred while exporting the report: " + e.getMessage());
            }
        }
    }
    
    // Menu actions
    @FXML
    private void showAdminLogin(ActionEvent event) {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/login.fxml"));
            Parent loginView = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the login scene
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
            stage.setTitle("Travel Agency Admin Login");
            stage.setWidth(600);
            stage.setHeight(400);
            stage.centerOnScreen();
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error", 
                    "Error loading admin login screen: " + e.getMessage());
        }
    }
    
    @FXML
    private void showAboutDialog(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "About", "Travel Agency Booking System", 
                "Version 1.0\nDeveloped for Java Programming course\n2025");
    }
    
    // Admin login
    @FXML
    private void loginAdmin() {
        String username = adminUsernameField.getText();
        String password = adminPasswordField.getText();
        
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            adminErrorLabel.setText("Please enter both username and password");
            adminErrorLabel.setVisible(true);
            return;
        }
        
        // Simple admin authentication for demonstration
        // In a real application, you would use proper authentication mechanisms
        if (username.equals("admin") && password.equals("admin123")) {
            isAdminAuthenticated = true;
            showAdminPanel();
            adminErrorLabel.setVisible(false);
            updateStatus("Admin logged in successfully");
        } else {
            isAdminAuthenticated = false;
            adminErrorLabel.setText("Invalid username or password");
            adminErrorLabel.setVisible(true);
            updateStatus("Admin login failed");
        }
    }
    
    private void showAdminPanel() {
        try {
            // Load the admin panel
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/admin_panel.fxml"));
            Node adminPanel = loader.load();
            
            // Clear the current content and show the admin panel
            adminContainer.setCenter(adminPanel);
            
            // Get the controller for additional setup if needed
            AdminController adminController = loader.getController();
            // You can pass data to the admin controller if needed
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Admin Panel Error", 
                    "Error loading admin panel: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void logoutAdmin() {
        isAdminAuthenticated = false;
        adminContainer.setCenter(adminLoginBox);
        adminUsernameField.clear();
        adminPasswordField.clear();
        adminErrorLabel.setVisible(false);
        updateStatus("Admin logged out");
    }
    
    // Helper methods
    private void updateStatus(String message) {
        System.out.println(message);
        statusLabel.setText(message);
    }
    
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    // Helper method to format dates
    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
} 