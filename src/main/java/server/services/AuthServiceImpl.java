package server.services;

import server.Server; // ✅ ADD this import
import server.database.DatabaseConnection;
import shared.classes.SessionManager;
import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.interfaces.AuthService;

import javax.naming.AuthenticationException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService, Serializable {
    private static final long serialVersionUID = 1L;

    private static Connection con = DatabaseConnection.setCon();
    private static String query;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    public AuthServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public User login(String userID, String password) throws RemoteException {
        String query = "SELECT userID, firstName, lastName, phoneNumber, email, role, password " +
                "FROM user WHERE userID = ? AND password = ?";

        try (Connection con = DatabaseConnection.setCon();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("userID"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getLong("phoneNumber"),
                            resultSet.getString("email"),
                            resultSet.getString("role"),
                            resultSet.getString("password")
                    );

                    Server.addActiveClient(userID);
                    System.out.println("[Auth] User logged in and tracked: " + userID);

                    return user;
                } else {
                    throw new AccountDoesNotExist("Account Does Not Exist in the DATABASE");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Database error during login: " + e.getMessage());
            try {
                // Test if reconnection is possible
                DatabaseConnection.testConnection();
                // If successful, retry the operation
                return login(userID, password);
            } catch (SQLException ex) {
                throw new RemoteException("Failed to reconnect to database", ex);
            }
        }
    }

    @Override
    public User logout() throws RemoteException {
        String userId = SessionManager.getCurrentUserId();
        if (userId != null) {
            Server.removeActiveClient(userId); // Remove client from active list
            System.out.println("[SERVER | "+ new Date()+ "] User logged out and removed: " + userId);
            SessionManager.endSession();
        }
        return null;
    }
}
