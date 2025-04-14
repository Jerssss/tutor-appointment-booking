package client.landingpage.login;

import client.AdminClient;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;
import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.exceptions.AlreadyLoggedInException;
import shared.interfaces.AuthService;

import static client.AdminClient.getAuthService;

public class AdminLoginController {
    private final AdminLoginView adminLoginView; // The associated view for the login page
    private final AdminLoginModel adminLoginModel; // The associated model for the login page

    public AdminLoginController(AdminLoginView adminLoginView, AdminLoginModel adminLoginModel, AuthService authService) {
        this.adminLoginView = adminLoginView;
        this.adminLoginModel = adminLoginModel;

        this.adminLoginView.setLogInPageLogInButton(this::handleSignIn);
    }

    private void handleSignIn(ActionEvent event) {
        String userID = adminLoginView.getIdField().getText().trim();
        String password = adminLoginView.getPassField().getText().trim();

        if (userID.isEmpty() || password.isEmpty()) {
            adminLoginView.setPromptLabel("Please complete all fields.");
            adminLoginView.setPromptLabelVisible(true);
            return;
        }

        try {
            User user = AdminClient.getAuthService().login(userID, password);
            if (user != null && "Admin".equalsIgnoreCase(user.getRole())) {
                System.out.println("[INFO] Login successful for user: " + user.getUserID());
                adminLoginView.setPromptLabel("Login successful!");
                adminLoginView.setPromptLabelVisible(true);
                redirectToMainMenu(user);

            } else {
                adminLoginView.setPromptLabel("Invalid credentials. Please try again.");
            }
        } catch (AccountDoesNotExist e) {
            adminLoginView.setPromptLabel("Invalid email or password. Please try again.");
            adminLoginView.setPromptLabelVisible(true);
        } catch (AlreadyLoggedInException e) {
            adminLoginView.setPromptLabel("Account was logged in elsewhere. You are now logged in.");
            adminLoginView.setPromptLabelVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred during login. Please try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redirectToMainMenu(User user) {

    }
}