package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/learnify?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection setCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("autoReconnect", "true");
            props.setProperty("maxReconnects", "10");
            props.setProperty("initialTimeout", "5");

            return DriverManager.getConnection(URL, props);

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


    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }

    public static synchronized void testConnection() throws SQLException {
        setCon().createStatement().execute("SELECT 1");
    }

    private static void handleConnectionFailure() {
        // you can show an alert popup or log, depending on your UI
        System.err.println("Handling connection failure...");
    }
}
