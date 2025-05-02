package com.bookify.app.controller;

import com.bookify.app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the admin login screen
 */
public class LoginController {

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    // Hardcoded credentials - in a real app, this would be stored securely
    private final Map<String, String> validCredentials = new HashMap<>();
    
    public LoginController() {
        // Initialize valid credentials
        validCredentials.put("admin", "admin123");
        validCredentials.put("manager", "manager123");
    }
    
    @FXML
    public void initialize() {
        // Hide error message initially
        errorLabel.setVisible(false);
        
        // Add listeners for input fields to hide error when user types
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setVisible(false);
        });
        
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setVisible(false);
        });
    }
    
    @FXML
    void login(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (isValidLogin(username, password)) {
            try {
                // Load admin dashboard
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/admin.fxml"));
                Parent adminView = loader.load();
                
                // Get access to the controller
                AdminController adminController = loader.getController();
                adminController.setCurrentUser(username);
                
                // Get the current stage
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                
                // Set the admin scene
                Scene scene = new Scene(adminView);
                stage.setScene(scene);
                stage.setTitle("Travel Agency Admin Dashboard");
                stage.setMaximized(true);
                stage.show();
                
            } catch (IOException e) {
                System.err.println("Error loading admin view: " + e.getMessage());
                e.printStackTrace();
                errorLabel.setText("Error loading admin page");
                errorLabel.setVisible(true);
            }
        } else {
            // Display error message
            errorLabel.setText("Invalid username or password");
            errorLabel.setVisible(true);
            passwordField.clear();
        }
    }
    
    @FXML
    void cancel(ActionEvent event) {
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
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error loading main view: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean isValidLogin(String username, String password) {
        // Check if username exists and password matches
        return validCredentials.containsKey(username) && 
               validCredentials.get(username).equals(password);
    }
} 