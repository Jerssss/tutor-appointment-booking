package client.landingpage.login;

import javafx.event.ActionEvent;
import javax.swing.JOptionPane;
import shared.classes.User;
import shared.interfaces.AuthService;

public class AdminLoginController {
    private final AdminLoginView adminLoginView; // The associated view for the login page
    private final AdminLoginModel adminLoginModel; // The associated model for the login page
    private final AuthService authService; // Authentication service

    public AdminLoginController(AdminLoginView adminLoginView, AdminLoginModel adminLoginModel, AuthService authService) {
        this.adminLoginView = adminLoginView;
        this.adminLoginModel = adminLoginModel;
        this.authService = authService;

        this.adminLoginView.setActionSignInButton(this::handleSignIn);
    }

    private void handleSignIn(ActionEvent event) {
        String email = adminLoginView.getIdField().getText();
        String password = adminLoginView.getPassField().getText();

        if (email.isEmpty() || password.isEmpty()) {
            adminLoginView.setPromptLabel("Please complete all fields.");
            return;
        }

        try {
            User user = authService.login(email, password);
            if (user != null && "Admin".equalsIgnoreCase(user.getRole())) {
                adminLoginView.setPromptLabel("Login successful!");
                // Redirect to Admin Main Menu
            } else {
                adminLoginView.setPromptLabel("Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred during login. Please try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}