package server.services;

import server.database.DatabaseConnection;
import shared.classes.User;
import shared.interfaces.AuthService;

import javax.naming.AuthenticationException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    public AuthServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public User login(String email, String password) throws RemoteException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getCon();
            String query = "SELECT userID, firstName, lastName, phoneNumber, email, role " +
                    "FROM users WHERE email = ? AND password = ?";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password); // In production, use hashed passwords!

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Create a new User object based on the retrieved data
                return new User(
                        rs.getString("userID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getLong("phoneNumber"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            } else {
                throw new AuthenticationException("Account Does Not Exist in the DATABASE");
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error during login", e);
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { }
            // Don't close the connection as it's a static shared connection
        }
    }

    @Override
    public User logout() {
        return null;
    }
}