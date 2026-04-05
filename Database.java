package com.childcareapp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
public class Database {
    private static Connection connection;

    public static void initializeDatabase() {
        try {
            File dbFile = new File("hotel_booking.db");
            System.out.println("📁 DB file location: " + dbFile.getAbsolutePath());
            connection = DriverManager.getConnection("jdbc:sqlite:"+ dbFile.getAbsolutePath());
            System.out.println("✅ Connected to SQLite!");

            Statement stmt = connection.createStatement();

            // Create Customers table
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT UNIQUE)");

            // Create Bookings table
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INTEGER PRIMARY KEY, " +
                    "customer_id INTEGER, " +
                    "room_number INTEGER, " +
                    "check_in TEXT, " +
                    "check_out TEXT, " +
                    "FOREIGN KEY(customer_id) REFERENCES customers(id))");

            System.out.println("✅ Database initialized successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}


