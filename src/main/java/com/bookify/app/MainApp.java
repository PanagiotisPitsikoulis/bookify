package com.bookify.app;

import java.io.IOException;

import com.bookify.app.database.DatabaseConnection;
import com.bookify.app.database.SampleDataGenerator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

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

            // Load FXML with more detailed error handling
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main.fxml"));

            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Travel Agency Booking System");
                primaryStage.setWidth(1000);
                primaryStage.setHeight(700);
                primaryStage.centerOnScreen();
                primaryStage.show();

                System.out.println("JavaFX application started successfully");

            } catch (IOException ex) {
                System.err.println("Error loading FXML: " + ex.getMessage());
                ex.printStackTrace();

                Platform.runLater(() -> {
                    String errorDetails = "Error: " + ex.getMessage();
                    if (ex.getCause() != null) {
                        errorDetails += "\nCaused by: " + ex.getCause().getMessage();
                    }

                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Failed to load UI: " + errorDetails,
                            ButtonType.OK);
                    alert.showAndWait();
                    Platform.exit();
                });
            }

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

            // Generate sample data if needed
            if (args.length > 0 && args[0].equals("--generate-data")) {
                SampleDataGenerator.generateSampleData();
            }

            // Launch JavaFX application
            launch(args);
        } catch (Exception e) {
            System.err.println("Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}