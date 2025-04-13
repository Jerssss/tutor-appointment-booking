package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection con;

    private DatabaseConnection (){
        //Yeah this Constructor
    }

    public static void setCon() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/learnify?user=root&password=";
            con = DriverManager.getConnection(url);
            System.out.println("[Database] Connection established.");
        } catch (SQLException e) {
            System.err.println("[Database ERROR] Connection failed: " + e.getMessage());
            throw e; // Re-throw to prevent server from starting
        } catch (ClassNotFoundException e) {
            System.err.println("[Database ERROR] MySQL Driver not found: " + e.getMessage());
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    public static Connection getCon() {
        return con;
    }
}
