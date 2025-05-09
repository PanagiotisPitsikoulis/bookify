package com.bookify.app.controller;

import java.io.IOException;

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

    // Store the current theme to apply to other screens
    private String currentTheme = "mocha-theme";

    @FXML
    private void initialize() {
        // Initialize login screen
        errorLabel.setVisible(false);
    }

    @FXML
    private void login(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        // Check login credentials
        // This is a simple check, in a real app you'd want to hash passwords and check
        // against a DB
        if (!isValidLogin(username, password)) {
            showError("Invalid username or password");
            return;
        }

        // If it's a valid login, proceed to admin panel
        loadAdminPanel(event);
    }

    private boolean isValidLogin(String username, String password) {
        // Simple admin authentication for demonstration
        return username.equals("admin") && password.equals("admin123");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void loadAdminPanel(ActionEvent event) {
        try {
            // Load the admin view
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/admin.fxml"));
            Parent adminView = loader.load();

            // Apply current theme
            adminView.getStyleClass().removeAll("latte-theme", "frappe-theme", "macchiato-theme", "mocha-theme");
            adminView.getStyleClass().add(currentTheme);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create and set the scene
            Scene scene = new Scene(adminView);
            stage.setScene(scene);
            stage.setTitle("Travel Agency Admin Panel");
            stage.setWidth(1200);
            stage.setHeight(800);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            showError("Error loading admin panel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        try {
            // Load the main app view
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/main.fxml"));
            Parent mainView = loader.load();

            // Apply current theme
            mainView.getStyleClass().removeAll("latte-theme", "frappe-theme", "macchiato-theme", "mocha-theme");
            mainView.getStyleClass().add(currentTheme);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create and set the scene
            Scene scene = new Scene(mainView);
            stage.setScene(scene);
            stage.setTitle("Travel Agency Booking System");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            showError("Error loading main application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}