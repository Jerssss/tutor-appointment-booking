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
            String url = "jdbc:mysql://localhost:3306/learnify?user=root&password=";
            con = DriverManager.getConnection(url);
            System.out.println("[Database] Connection established.");
        } catch (SQLException e) {
            System.err.println("[Database ERROR] Connection failed: " + e.getMessage());
            throw e; // Re-throw to prevent server from starting
        }
    }

    public static Connection getCon() {
        return con;
    }
}
