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
            System.out.println("Failed to establish database connection. Details:");
            if (e instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException) {
                System.out.println("Connection refused. Possible causes:");
                System.out.println("1. MySQL server is not running");
                System.out.println("2. Incorrect connection URL or port");
                System.out.println("3. Firewall blocking the connection");
            } else {
                System.out.println("SQL Exception: " + e.getMessage());
                System.out.println("Please check your database credentials and connection URL");
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