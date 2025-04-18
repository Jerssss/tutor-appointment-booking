package server.services;

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

public class AuthServiceImpl extends UnicastRemoteObject implements AuthService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID

    private static Connection con = DatabaseConnection.setCon();

    private static String query;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    public AuthServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public User login(String userID, String password) throws RemoteException {
        query = "SELECT userID, firstName, lastName, phoneNumber, email, role " +
                "FROM user WHERE userID = ? AND password = ?";

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Create a new User object based on the retrieved data
                return new User(
                        resultSet.getString("userID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getLong("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                );
            } else {
                throw new AccountDoesNotExist("Account Does Not Exist in the DATABASE");
            }
        } catch (SQLException e) {
            throw new RemoteException("Database error during login", e);
        }
    }

    @Override
    public User logout() throws RemoteException {
        String userId = SessionManager.getCurrentUserId();
        if (userId != null) {
            SessionManager.endSession();
        }
        return null;
    }
}