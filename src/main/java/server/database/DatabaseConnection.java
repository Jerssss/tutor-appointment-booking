package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL =  "jdbc:mysql://localhost:3306/learnify?user=root&password=";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection setCon() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection established successfully.");
            return connection;
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please ensure the driver is in the classpath.");
            e.printStackTrace();
            handleConnectionFailure();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection. Details:");
            if (e instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException) {
                System.err.println("Connection refused. Possible causes:");
                System.err.println("1. MySQL server is not running");
                System.err.println("2. Incorrect connection URL or port");
                System.err.println("3. Firewall blocking the connection");
            } else {
                System.err.println("SQL Exception: " + e.getMessage());
                System.err.println("Please check your database credentials and connection URL");
            }
            e.printStackTrace();
            handleConnectionFailure();
        }
        return connection;
    }

    private static void handleConnectionFailure() {
        System.out.println("Database connection failed. Server cannot start without database access.");
    }
}