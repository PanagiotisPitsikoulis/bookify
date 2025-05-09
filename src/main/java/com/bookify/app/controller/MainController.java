package com.bookify.app.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bookify.app.MainApp;
import com.bookify.app.dao.BookingDAO;
import com.bookify.app.dao.CustomerDAO;
import com.bookify.app.dao.DestinationDAO;
import com.bookify.app.model.Booking;
import com.bookify.app.model.Customer;
import com.bookify.app.model.Destination;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    // DAOs
    private CustomerDAO customerDAO;
    private DestinationDAO destinationDAO;
    private BookingDAO bookingDAO;

    // Customer tab
    @FXML
    private TextField customerSearchField;
    @FXML
    private ListView<Customer> customerListView;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerEmailField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TableView<Booking> customerBookingsTable;
    @FXML
    private TableColumn<Booking, Integer> customerBookingIdColumn;
    @FXML
    private TableColumn<Booking, String> customerBookingDestinationColumn;
    @FXML
    private TableColumn<Booking, String> customerBookingDateColumn;
    @FXML
    private TableColumn<Booking, String> customerBookingStatusColumn;

    // Destination tab
    @FXML
    private TextField destinationSearchField;
    @FXML
    private ListView<Destination> destinationListView;
    @FXML
    private TextField destinationNameField;
    @FXML
    private TextField destinationCountryField;
    @FXML
    private TextField destinationDescriptionField;
    @FXML
    private TextField destinationPriceField;
    @FXML
    private String selectedIcon = "🏝️"; // Default icon

    // Booking tab
    @FXML
    private TextField bookingSearchField;
    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> bookingCustomerColumn;
    @FXML
    private TableColumn<Booking, String> bookingDestinationColumn;
    @FXML
    private TableColumn<Booking, String> bookingTravelDateColumn;
    @FXML
    private TableColumn<Booking, Integer> bookingPeopleColumn;
    @FXML
    private TableColumn<Booking, Double> bookingPriceColumn;
    @FXML
    private TableColumn<Booking, String> bookingStatusColumn;

    // Reports tab
    @FXML
    private Label reportTitleLabel;
    @FXML
    private TableView<ReportRow> reportTable;

    // Admin tab
    @FXML
    private BorderPane adminContainer;
    @FXML
    private VBox adminLoginBox;
    @FXML
    private TextField adminUsernameField;
    @FXML
    private PasswordField adminPasswordField;
    @FXML
    private Label adminErrorLabel;

    // Status
    @FXML
    private Label statusLabel;

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

    // Dashboard components
    @FXML
    private Label dashboardDestinationsCount;
    @FXML
    private Label dashboardCustomersCount;
    @FXML
    private Label dashboardBookingsCount;
    @FXML
    private TableView<Booking> dashboardUpcomingBookingsTable;
    @FXML
    private TableColumn<Booking, Integer> dashboardBookingIdColumn;
    @FXML
    private TableColumn<Booking, String> dashboardBookingCustomerColumn;
    @FXML
    private TableColumn<Booking, String> dashboardBookingDestinationColumn;
    @FXML
    private TableColumn<Booking, String> dashboardBookingTravelDateColumn;
    @FXML
    private TableColumn<Booking, String> dashboardBookingStatusColumn;

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

        // Update dashboard with real data
        updateDashboard();

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

        // Dashboard upcoming bookings table
        dashboardBookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dashboardBookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        dashboardBookingDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinationName"));
        dashboardBookingTravelDateColumn.setCellValueFactory(new PropertyValueFactory<>("travelDate"));
        dashboardBookingStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
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
        try {
            // Load the booking dialog
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/booking_dialog.fxml"));
            Parent dialogRoot = loader.load();

            // Get the controller
            BookingDialogController controller = loader.getController();

            // Set callback for when the dialog is completed
            controller.setCallback(new BookingDialogController.BookingDialogCallback() {
                @Override
                public void onBookingSaved(Booking booking) {
                    // Add the new booking to the list and refresh
                    bookings.add(booking);
                    updateDashboard();
                    updateStatus("New booking created successfully");
                }

                @Override
                public void onDialogCancelled() {
                    updateStatus("Booking creation cancelled");
                }
            });

            // Create and show the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Booking");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookingsTable.getScene().getWindow());

            Scene scene = new Scene(dialogRoot);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Dialog Error",
                    "Error creating new booking dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void editBooking() {
        if (selectedBooking == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "No Booking Selected",
                    "Please select a booking to edit.");
            return;
        }

        try {
            // Load the booking dialog
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/booking_dialog.fxml"));
            Parent dialogRoot = loader.load();

            // Get the controller
            BookingDialogController controller = loader.getController();

            // Set the booking to edit
            controller.setBooking(selectedBooking);

            // Set callback for when the dialog is completed
            controller.setCallback(new BookingDialogController.BookingDialogCallback() {
                @Override
                public void onBookingSaved(Booking booking) {
                    // Update the booking in the list and refresh
                    int index = bookings.indexOf(selectedBooking);
                    if (index >= 0) {
                        bookings.set(index, booking);
                    }
                    updateDashboard();
                    updateStatus("Booking #" + booking.getId() + " updated successfully");
                }

                @Override
                public void onDialogCancelled() {
                    updateStatus("Booking edit cancelled");
                }
            });

            // Create and show the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Booking #" + selectedBooking.getId());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bookingsTable.getScene().getWindow());

            Scene scene = new Scene(dialogRoot);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Dialog Error",
                    "Error creating edit booking dialog: " + e.getMessage());
            e.printStackTrace();
        }
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

        try {
            // Get all customers
            List<Customer> customers = customerDAO.getAllCustomers();

            // Create report data
            ObservableList<ReportRow> reportData = FXCollections.observableArrayList();

            for (Customer customer : customers) {
                // Get bookings for this customer
                List<Booking> customerBookings = bookingDAO.getBookingsByCustomerId(customer.getId());

                // Count destinations visited
                long destinationsVisited = customerBookings.stream()
                        .map(Booking::getDestinationId)
                        .distinct()
                        .count();

                // Calculate total spent
                double totalSpent = customerBookings.stream()
                        .mapToDouble(Booking::getTotalPrice)
                        .sum();

                // Add to report
                ReportRow row = new ReportRow(
                        String.valueOf(customer.getId()),
                        customer.getName(),
                        customer.getEmail(),
                        String.valueOf(customerBookings.size()),
                        String.valueOf(destinationsVisited),
                        String.format("$%.2f", totalSpent));

                reportData.add(row);
            }

            // Set up report table
            setupReportTable(
                    "ID", "Customer Name", "Email", "Total Bookings",
                    "Destinations Visited", "Total Spent");

            // Add data to table
            reportTable.setItems(reportData);

            updateStatus("Generated customer travel history report");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Error", "Error Generating Report",
                    "An error occurred while generating the customer report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void generateDestinationReport() {
        reportTitleLabel.setText("Popular Destinations Report");

        try {
            // Get all destinations
            List<Destination> destinations = destinationDAO.getAllDestinations();

            // Get all bookings
            List<Booking> allBookings = bookingDAO.getAllBookings();

            // Create report data
            ObservableList<ReportRow> reportData = FXCollections.observableArrayList();

            for (Destination destination : destinations) {
                // Count bookings for this destination
                long bookingCount = allBookings.stream()
                        .filter(b -> b.getDestinationId() == destination.getId())
                        .count();

                // Calculate revenue from this destination
                double revenue = allBookings.stream()
                        .filter(b -> b.getDestinationId() == destination.getId())
                        .mapToDouble(Booking::getTotalPrice)
                        .sum();

                // Get most recent booking date
                String lastBookingDate = allBookings.stream()
                        .filter(b -> b.getDestinationId() == destination.getId())
                        .map(Booking::getTravelDate)
                        .max(String::compareTo)
                        .orElse("N/A");

                // Add to report
                ReportRow row = new ReportRow(
                        String.valueOf(destination.getId()),
                        destination.getName(),
                        destination.getCountry(),
                        String.format("$%.2f", destination.getPricePerPerson()),
                        String.valueOf(bookingCount),
                        String.format("$%.2f", revenue));

                reportData.add(row);
            }

            // Sort by booking count descending
            reportData.sort((r1, r2) -> {
                try {
                    long count1 = Long.parseLong(r1.getData5());
                    long count2 = Long.parseLong(r2.getData5());
                    return Long.compare(count2, count1);
                } catch (NumberFormatException e) {
                    // Fallback comparison if parsing fails
                    return r2.getData5().compareTo(r1.getData5());
                }
            });

            // Set up report table
            setupReportTable(
                    "ID", "Destination", "Country", "Price",
                    "Booking Count", "Total Revenue");

            // Add data to table
            reportTable.setItems(reportData);

            updateStatus("Generated popular destinations report");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Error", "Error Generating Report",
                    "An error occurred while generating the destinations report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void generateBookingReport() {
        reportTitleLabel.setText("Booking Summary Report");

        try {
            // Get all bookings
            List<Booking> allBookings = bookingDAO.getAllBookings();

            // Create report data
            ObservableList<ReportRow> reportData = FXCollections.observableArrayList();

            // Group by status
            Map<String, List<Booking>> bookingsByStatus = allBookings.stream()
                    .collect(Collectors.groupingBy(Booking::getStatus));

            // Add row for each status
            for (Map.Entry<String, List<Booking>> entry : bookingsByStatus.entrySet()) {
                String status = entry.getKey();
                List<Booking> statusBookings = entry.getValue();

                // Calculate revenue for this status
                double revenue = statusBookings.stream()
                        .mapToDouble(Booking::getTotalPrice)
                        .sum();

                // Calculate average booking price
                double avgPrice = revenue / statusBookings.size();

                // Add to report
                ReportRow row = new ReportRow(
                        status,
                        String.valueOf(statusBookings.size()),
                        String.format("$%.2f", revenue),
                        String.format("$%.2f", avgPrice),
                        String.format("%.1f%%", (double) statusBookings.size() / allBookings.size() * 100),
                        "");

                reportData.add(row);
            }

            // Add a total row
            double totalRevenue = allBookings.stream()
                    .mapToDouble(Booking::getTotalPrice)
                    .sum();

            double avgPrice = totalRevenue / allBookings.size();

            ReportRow totalRow = new ReportRow(
                    "TOTAL",
                    String.valueOf(allBookings.size()),
                    String.format("$%.2f", totalRevenue),
                    String.format("$%.2f", avgPrice),
                    "100.0%",
                    "");

            reportData.add(totalRow);

            // Set up report table
            setupReportTable(
                    "Status", "Number of Bookings", "Total Revenue",
                    "Average Price", "Percentage", "");

            // Add data to table
            reportTable.setItems(reportData);

            updateStatus("Generated booking summary report");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Error", "Error Generating Report",
                    "An error occurred while generating the booking report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void generateRevenueReport() {
        reportTitleLabel.setText("Revenue Report");

        try {
            // Get all bookings
            List<Booking> allBookings = bookingDAO.getAllBookings();

            // Create report data
            ObservableList<ReportRow> reportData = FXCollections.observableArrayList();

            // Group by destination ID
            Map<Integer, List<Booking>> bookingsByDestination = allBookings.stream()
                    .collect(Collectors.groupingBy(Booking::getDestinationId));

            // Get all destinations for reference
            Map<Integer, Destination> destinationsById = new HashMap<>();
            for (Destination destination : destinationDAO.getAllDestinations()) {
                destinationsById.put(destination.getId(), destination);
            }

            // Calculate revenue by destination
            for (Map.Entry<Integer, List<Booking>> entry : bookingsByDestination.entrySet()) {
                int destinationId = entry.getKey();
                List<Booking> destBookings = entry.getValue();

                // Get destination name
                Destination destination = destinationsById.get(destinationId);
                String destinationName = destination != null ? destination.getName() + ", " + destination.getCountry()
                        : "Unknown Destination";

                // Calculate metrics
                double totalRevenue = destBookings.stream()
                        .mapToDouble(Booking::getTotalPrice)
                        .sum();

                int totalPeople = destBookings.stream()
                        .mapToInt(Booking::getNumberOfPeople)
                        .sum();

                double revenuePerPerson = totalPeople > 0 ? totalRevenue / totalPeople : 0;

                // Add to report
                ReportRow row = new ReportRow(
                        String.valueOf(destinationId),
                        destinationName,
                        String.valueOf(destBookings.size()),
                        String.valueOf(totalPeople),
                        String.format("$%.2f", totalRevenue),
                        String.format("$%.2f", revenuePerPerson));

                reportData.add(row);
            }

            // Sort by total revenue descending
            reportData.sort((r1, r2) -> {
                double rev1 = Double.parseDouble(r1.getData5().replace("$", ""));
                double rev2 = Double.parseDouble(r2.getData5().replace("$", ""));
                return Double.compare(rev2, rev1);
            });

            // Set up report table
            setupReportTable(
                    "ID", "Destination", "Bookings",
                    "Total People", "Total Revenue", "Revenue Per Person");

            // Add data to table
            reportTable.setItems(reportData);

            updateStatus("Generated revenue report");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Error", "Error Generating Report",
                    "An error occurred while generating the revenue report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set up the report table with the given column headers
     */
    private void setupReportTable(String col1, String col2, String col3,
            String col4, String col5, String col6) {
        // Clear existing columns
        reportTable.getColumns().clear();

        // Create new columns with the given headers
        TableColumn<ReportRow, String> column1 = new TableColumn<>(col1);
        column1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData1()));

        TableColumn<ReportRow, String> column2 = new TableColumn<>(col2);
        column2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData2()));

        TableColumn<ReportRow, String> column3 = new TableColumn<>(col3);
        column3.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData3()));

        TableColumn<ReportRow, String> column4 = new TableColumn<>(col4);
        column4.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData4()));

        TableColumn<ReportRow, String> column5 = new TableColumn<>(col5);
        column5.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData5()));

        TableColumn<ReportRow, String> column6 = new TableColumn<>(col6);
        column6.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getData6()));

        // Set preferred widths
        column1.setPrefWidth(80);
        column2.setPrefWidth(150);
        column3.setPrefWidth(150);
        column4.setPrefWidth(150);
        column5.setPrefWidth(150);
        column6.setPrefWidth(150);

        // Add columns to table
        reportTable.getColumns().addAll(column1, column2, column3, column4, column5, column6);
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
                // Write report title
                writer.write(reportTitleLabel.getText() + "\n\n");

                // Write column headers
                List<TableColumn<ReportRow, ?>> columns = reportTable.getColumns();
                StringBuilder headerLine = new StringBuilder();
                for (TableColumn<?, ?> column : columns) {
                    headerLine.append(column.getText()).append(",");
                }
                if (!headerLine.isEmpty()) {
                    headerLine.setLength(headerLine.length() - 1); // Remove trailing comma
                }
                writer.write(headerLine.toString() + "\n");

                // Write data rows
                for (ReportRow row : reportTable.getItems()) {
                    StringBuilder dataLine = new StringBuilder();
                    dataLine.append(row.getData1()).append(",");
                    dataLine.append(row.getData2()).append(",");
                    dataLine.append(row.getData3()).append(",");
                    dataLine.append(row.getData4()).append(",");
                    dataLine.append(row.getData5()).append(",");
                    dataLine.append(row.getData6()).append(",");

                    if (!dataLine.isEmpty()) {
                        dataLine.setLength(dataLine.length() - 1); // Remove trailing comma
                    }

                    writer.write(dataLine.toString() + "\n");
                }

                showAlert(Alert.AlertType.INFORMATION, "Export", "Report Exported",
                        "The report has been exported to " + file.getAbsolutePath());
                updateStatus("Report exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Export Error",
                        "An error occurred while exporting the report: " + e.getMessage());
            }
        }
    }

    /**
     * Helper class for report data
     */
    private static class ReportRow {
        private final String data1;
        private final String data2;
        private final String data3;
        private final String data4;
        private final String data5;
        private final String data6;

        public ReportRow(String data1, String data2, String data3,
                String data4, String data5, String data6) {
            this.data1 = data1;
            this.data2 = data2;
            this.data3 = data3;
            this.data4 = data4;
            this.data5 = data5;
            this.data6 = data6;
        }

        public String getData1() {
            return data1;
        }

        public String getData2() {
            return data2;
        }

        public String getData3() {
            return data3;
        }

        public String getData4() {
            return data4;
        }

        public String getData5() {
            return data5;
        }

        public String getData6() {
            return data6;
        }
    }

    // Menu actions
    @FXML
    private void showAdminLogin(ActionEvent event) {
        try {
            // Print detailed resource info
            System.out.println("Attempting to load login screen");
            String resourcePath = "/login.fxml";
            System.out.println("Resource path: " + resourcePath);
            System.out.println("Resource URL: " + MainApp.class.getResource(resourcePath));

            // Load the login screen
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(resourcePath));
            System.out.println("FXMLLoader created");

            try {
                Parent loginView = loader.load();
                System.out.println("Login view loaded successfully");

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
                System.out.println("Login screen displayed");
            } catch (IOException e) {
                System.err.println("Error loading login.fxml: " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("Caused by: " + e.getCause().getMessage());
                    e.getCause().printStackTrace();
                }
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error",
                        "Error loading admin login screen: " + e.getMessage() +
                                "\nCause: " + (e.getCause() != null ? e.getCause().getMessage() : "Unknown"));
            }
        } catch (Exception e) {
            System.err.println("Unexpected error in showAdminLogin: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Navigation Error",
                    "Unexpected error loading admin login screen: " + e.getMessage());
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

    // Icon selector methods
    @FXML
    public void selectBeachIcon() {
        selectedIcon = "🏝️";
        updateStatus("Beach icon selected");
    }

    @FXML
    public void selectCityIcon() {
        selectedIcon = "🏙️";
        updateStatus("City icon selected");
    }

    @FXML
    public void selectMountainIcon() {
        selectedIcon = "🏔️";
        updateStatus("Mountain icon selected");
    }

    @FXML
    public void selectCastleIcon() {
        selectedIcon = "🏰";
        updateStatus("Castle icon selected");
    }

    @FXML
    public void selectTowerIcon() {
        selectedIcon = "🗼";
        updateStatus("Tower icon selected");
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

    /**
     * Updates the dashboard with real-time data
     */
    private void updateDashboard() {
        try {
            // Update statistics counters with real data
            int destinationCount = destinations.size();
            int customerCount = customers.size();
            int bookingCount = bookings.size();

            dashboardDestinationsCount.setText(destinationCount + " available");
            dashboardCustomersCount.setText(customerCount + " registered");
            dashboardBookingsCount.setText(bookingCount + " total");

            // Get upcoming bookings (those with status "Pending" or "Confirmed")
            List<Booking> upcomingBookings = bookings.stream()
                    .filter(b -> b.getStatus().equals("Pending") || b.getStatus().equals("Confirmed"))
                    .sorted((b1, b2) -> b1.getTravelDate().compareTo(b2.getTravelDate()))
                    .limit(10) // Show only the next 10 bookings
                    .collect(Collectors.toList());

            // Update the dashboard table
            dashboardUpcomingBookingsTable.setItems(FXCollections.observableArrayList(upcomingBookings));

            updateStatus("Dashboard updated with real-time data");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Dashboard Update Error",
                    "An error occurred while updating the dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshDashboard() {
        try {
            // Reload all data from the database
            loadData();

            // Update the dashboard
            updateDashboard();

            updateStatus("Dashboard refreshed successfully");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Refresh Error",
                    "An error occurred while refreshing data: " + e.getMessage());
        }
    }
}