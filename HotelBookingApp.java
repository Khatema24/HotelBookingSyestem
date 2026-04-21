package com.childcareapp;

import com.childcareapp.Database.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class HotelBookingApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database
        Database.initializeDatabase();

        // UI Elements
        Label fullName = new Label("Customer Name:");
        TextField nameField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Key registerButton = new Button("Register");
        registerButton.setOnAction(e ->
                registerCustomer(nameField.getText(), emailField.getText()));

        Label roomLabel = new Label("Room Number:");
        TextField roomField = new TextField();

        Label checkInLabel = new Label("Check-In Date:");
        TextField checkInField = new TextField("YYYY-MM-DD");

        Label checkOutLabel = new Label("Check-Out Date:");
        TextField checkOutField = new TextField("YYYY-MM-DD");

        Button bookButton = new Button("Book Room");
        bookButton.setOnAction(e ->
                bookRoom(emailField.getText(), roomField.getText(), checkInField.getText(), checkOutField.getText()));

        VBox layout = new VBox(10,
                nameLabel, nameField,
                emailLabel, emailField,
                registerButton,
                roomLabel, roomField,
                checkInLabel, checkInField,
                checkOutLabel, checkOutField,
                bookButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("Hotel Booking System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerCustomer(String name, String email) {
        try {
            System.out.println("Registering customer...");
            System.out.println("Name: " + name);
            System.out.println("Email: " + email);

            Connection connection = Database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO customers (name, email) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();

            System.out.println("✅ Customer registered successfully!");
            showAlert("Success", "Customer registered successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Error: Customer already exists or invalid input.");
            showAlert("Error", "Customer already exists or invalid input.");
        }
    }

    private void bookRoom(String email, String roomNumber, String checkIn, String checkOut) {
        try {
            System.out.println("Booking room...");
            System.out.println("Email: " + email);
            System.out.println("Room Number: " + roomNumber);
            System.out.println("Check-in Date: " + checkIn);
            System.out.println("Check-out Date: " + checkOut);

            Connection connection = Database.getConnection();
            PreparedStatement findCustomer = connection.prepareStatement(
                    "SELECT id FROM customers WHERE email = ?");
            findCustomer.setString(1, email);
            ResultSet result = findCustomer.executeQuery();

            if (result.next()) {
                int customerId = result.getInt("id");

                PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO bookings (customer_id, room_number, check_in, check_out) VALUES (?, ?, ?, ?)");
                stmt.setInt(1, customerId);
                stmt.setInt(2, Integer.parseInt(roomNumber));
                stmt.setString(3, checkIn);
                stmt.setString(4, checkOut);
                stmt.executeUpdate();

                System.out.println("✅ Booking Successful!");
                showAlert("Success", "Room booked successfully!");
            } else {
                System.out.println("❌ Error: Customer not found!");
                showAlert("Error", "Customer not found. Register first.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ SQL Error: " + e.getMessage());
            showAlert("Error", "An SQL error occurred.");
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input: " + e.getMessage());
            showAlert("Error", "Room number must be a number.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



    


































