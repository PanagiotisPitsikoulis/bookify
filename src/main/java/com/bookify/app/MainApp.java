package com.bookify.app;

import com.bookify.app.database.DatabaseConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in thread: " + thread.getName());
            throwable.printStackTrace();
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "An unexpected error occurred: " + throwable.getMessage(), 
                    ButtonType.OK);
                alert.showAndWait();
            });
        });
        
        try {
            System.out.println("Starting application...");
            
            // Start with the main application instead of login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            
            // Set up the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Travel Agency Booking System");
            primaryStage.setWidth(1000);
            primaryStage.setHeight(700);
            primaryStage.centerOnScreen();
            primaryStage.show();
            
            System.out.println("JavaFX application started successfully");
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "Failed to load application UI: " + e.getMessage(), 
                    ButtonType.OK);
                alert.showAndWait();
                Platform.exit();
            });
        } catch (Exception e) {
            System.err.println("Critical application error: " + e.getMessage());
            e.printStackTrace();
            
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "Critical application error: " + e.getMessage(), 
                    ButtonType.OK);
                alert.showAndWait();
                Platform.exit();
            });
        }
    }
    
    @Override
    public void stop() {
        // Close database connection when application closes
        try {
            DatabaseConnection.closeConnection();
            System.out.println("JavaFX application stopped");
        } catch (Exception e) {
            System.err.println("Error while shutting down: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Establish database connection
            DatabaseConnection.getConnection();
            
            // Launch JavaFX application
            launch(args);
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 