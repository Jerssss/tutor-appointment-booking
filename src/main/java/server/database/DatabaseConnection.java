package server.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection con;

    private DatabaseConnection (){
        //Yeah this Constructor
    }

    public static void setCon() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/learnify?user=root&password");
        }catch (Exception e){
            System.out.println("Database Connection Failed.");
        }
    }
}
