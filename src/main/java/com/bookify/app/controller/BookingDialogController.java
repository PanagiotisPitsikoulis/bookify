package com.bookify.app.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bookify.app.dao.BookingDAO;
import com.bookify.app.dao.CustomerDAO;
import com.bookify.app.dao.DestinationDAO;
import com.bookify.app.model.Booking;
import com.bookify.app.model.Customer;
import com.bookify.app.model.Destination;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for the booking dialog window
 */
public class BookingDialogController {

    @FXML
    private Label titleLabel;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Destination> destinationComboBox;

    @FXML
    private DatePicker travelDatePicker;

    @FXML
    private Spinner<Integer> numberOfPeopleSpinner;

    @FXML
    private Label pricePerPersonLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField notesField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    private CustomerDAO customerDAO;
    private DestinationDAO destinationDAO;
    private BookingDAO bookingDAO;

    private Booking booking;
    private boolean isEditMode = false;
    private BookingDialogCallback callback;

    /**
     * Interface for callbacks when a booking is saved or cancelled
     */
    public interface BookingDialogCallback {
        void onBookingSaved(Booking booking);

        void onDialogCancelled();
    }

    /**
     * Initialize the dialog
     */
    @FXML
    private void initialize() {
        // Initialize DAOs
        customerDAO = new CustomerDAO();
        destinationDAO = new DestinationDAO();
        bookingDAO = new BookingDAO();

        // Hide error message initially
        errorLabel.setVisible(false);

        // Setup number of people spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        numberOfPeopleSpinner.setValueFactory(valueFactory);

        // Configure status options
        statusComboBox.setItems(FXCollections.observableArrayList(
                "Pending", "Confirmed", "Completed", "Cancelled"));
        statusComboBox.setValue("Pending");

        // Set current date as default
        travelDatePicker.setValue(LocalDate.now().plusDays(7)); // Default to one week from now

        // Setup change listeners to update price calculations
        setupChangeListeners();

        // Load customers and destinations
        loadCustomersAndDestinations();
    }

    /**
     * Configure change listeners for real-time price updates
     */
    private void setupChangeListeners() {
        // When destination changes, update price per person
        destinationComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                double price = newValue.getPricePerPerson();
                pricePerPersonLabel.setText(String.format("$%.2f", price));
                updateTotalPrice();
            } else {
                pricePerPersonLabel.setText("$0.00");
                totalPriceLabel.setText("$0.00");
            }
        });

        // When number of people changes, update total price
        numberOfPeopleSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateTotalPrice();
        });
    }

    /**
     * Calculate and update total price
     */
    private void updateTotalPrice() {
        Destination selectedDestination = destinationComboBox.getValue();
        Integer numberOfPeople = numberOfPeopleSpinner.getValue();

        if (selectedDestination != null && numberOfPeople != null) {
            double pricePerPerson = selectedDestination.getPricePerPerson();
            double totalPrice = pricePerPerson * numberOfPeople;
            totalPriceLabel.setText(String.format("$%.2f", totalPrice));
        } else {
            totalPriceLabel.setText("$0.00");
        }
    }

    /**
     * Load customers and destinations from the database
     */
    private void loadCustomersAndDestinations() {
        try {
            // Load customers
            List<Customer> customers = customerDAO.getAllCustomers();
            customerComboBox.setItems(FXCollections.observableArrayList(customers));

            // Set customer display mode
            customerComboBox.setConverter(new StringConverter<Customer>() {
                @Override
                public String toString(Customer customer) {
                    return customer == null ? "" : customer.getName();
                }

                @Override
                public Customer fromString(String string) {
                    return null; // Not used for combo box
                }
            });

            // Load destinations
            List<Destination> destinations = destinationDAO.getAllDestinations();
            destinationComboBox.setItems(FXCollections.observableArrayList(destinations));

            // Set destination display mode
            destinationComboBox.setConverter(new StringConverter<Destination>() {
                @Override
                public String toString(Destination destination) {
                    return destination == null ? "" : destination.getName() + ", " + destination.getCountry();
                }

                @Override
                public Destination fromString(String string) {
                    return null; // Not used for combo box
                }
            });

            // Select first items if available
            if (!customers.isEmpty()) {
                customerComboBox.setValue(customers.get(0));
            }

            if (!destinations.isEmpty()) {
                destinationComboBox.setValue(destinations.get(0));
            }
        } catch (Exception e) {
            showError("Error loading customers and destinations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Set the booking to edit (for edit mode)
     * 
     * @param booking The booking to edit
     */
    public void setBooking(Booking booking) {
        this.booking = booking;
        this.isEditMode = true;

        // Update title
        titleLabel.setText("Edit Booking");

        // Find and select the customer
        Customer bookingCustomer = findCustomerById(booking.getCustomerId());
        if (bookingCustomer != null) {
            customerComboBox.setValue(bookingCustomer);
        }

        // Find and select the destination
        Destination bookingDestination = findDestinationById(booking.getDestinationId());
        if (bookingDestination != null) {
            destinationComboBox.setValue(bookingDestination);
        }

        // Set travel date
        LocalDate travelDate = LocalDate.parse(booking.getTravelDate());
        travelDatePicker.setValue(travelDate);

        // Set number of people
        numberOfPeopleSpinner.getValueFactory().setValue(booking.getNumberOfPeople());

        // Set status
        statusComboBox.setValue(booking.getStatus());

        // Set notes
        notesField.setText(booking.getNotes());

        // Update price displays
        updateTotalPrice();
    }

    /**
     * Set the callback for when the dialog is completed
     * 
     * @param callback The callback to invoke
     */
    public void setCallback(BookingDialogCallback callback) {
        this.callback = callback;
    }

    /**
     * Find a customer by ID
     * 
     * @param id The customer ID
     * @return The customer or null if not found
     */
    private Customer findCustomerById(int id) {
        for (Customer customer : customerComboBox.getItems()) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find a destination by ID
     * 
     * @param id The destination ID
     * @return The destination or null if not found
     */
    private Destination findDestinationById(int id) {
        for (Destination destination : destinationComboBox.getItems()) {
            if (destination.getId() == id) {
                return destination;
            }
        }
        return null;
    }

    /**
     * Save the booking
     */
    @FXML
    private void saveBooking(ActionEvent event) {
        try {
            // Validate inputs
            if (!validateInputs()) {
                return;
            }

            Customer selectedCustomer = customerComboBox.getValue();
            Destination selectedDestination = destinationComboBox.getValue();
            LocalDate selectedDate = travelDatePicker.getValue();
            Integer people = numberOfPeopleSpinner.getValue();
            String status = statusComboBox.getValue();
            String notes = notesField.getText();

            // Calculate total price
            double pricePerPerson = selectedDestination.getPricePerPerson();
            double totalPrice = pricePerPerson * people;

            if (isEditMode) {
                // Update existing booking
                booking.setCustomerId(selectedCustomer.getId());
                booking.setCustomerName(selectedCustomer.getName());
                booking.setDestinationId(selectedDestination.getId());
                booking.setDestinationName(selectedDestination.getName() + ", " + selectedDestination.getCountry());
                booking.setTravelDate(selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                booking.setNumberOfPeople(people);
                booking.setPricePerPerson(pricePerPerson);
                booking.setTotalPrice(totalPrice);
                booking.setStatus(status);
                booking.setNotes(notes);

                boolean updated = bookingDAO.updateBooking(booking);

                if (!updated) {
                    showError("Failed to update booking in database");
                    return;
                }
            } else {
                // Create new booking
                booking = new Booking();
                booking.setCustomerId(selectedCustomer.getId());
                booking.setCustomerName(selectedCustomer.getName());
                booking.setDestinationId(selectedDestination.getId());
                booking.setDestinationName(selectedDestination.getName() + ", " + selectedDestination.getCountry());
                booking.setTravelDate(selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                booking.setNumberOfPeople(people);
                booking.setPricePerPerson(pricePerPerson);
                booking.setTotalPrice(totalPrice);
                booking.setStatus(status);
                booking.setNotes(notes);

                boolean added = bookingDAO.addBooking(booking);

                if (!added) {
                    showError("Failed to add booking to database");
                    return;
                }
            }

            // Notify callback and close dialog
            if (callback != null) {
                callback.onBookingSaved(booking);
            }

            closeDialog();
        } catch (Exception e) {
            showError("Error saving booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cancel the booking dialog
     */
    @FXML
    private void cancelBooking(ActionEvent event) {
        if (callback != null) {
            callback.onDialogCancelled();
        }
        closeDialog();
    }

    /**
     * Close the dialog window
     */
    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Validate all inputs
     * 
     * @return true if all inputs are valid
     */
    private boolean validateInputs() {
        if (customerComboBox.getValue() == null) {
            showError("Please select a customer");
            return false;
        }

        if (destinationComboBox.getValue() == null) {
            showError("Please select a destination");
            return false;
        }

        if (travelDatePicker.getValue() == null) {
            showError("Please select a travel date");
            return false;
        }

        if (travelDatePicker.getValue().isBefore(LocalDate.now())) {
            showError("Travel date cannot be in the past");
            return false;
        }

        if (numberOfPeopleSpinner.getValue() == null || numberOfPeopleSpinner.getValue() < 1) {
            showError("Please specify at least 1 person");
            return false;
        }

        if (statusComboBox.getValue() == null) {
            showError("Please select a status");
            return false;
        }

        // All inputs valid
        return true;
    }

    /**
     * Show error message
     * 
     * @param message The error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}