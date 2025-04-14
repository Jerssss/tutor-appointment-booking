package client.landingpage.login;

import javafx.event.ActionEvent;

import javax.naming.AuthenticationException;
import javax.swing.JOptionPane;
import shared.classes.User;
import shared.exceptions.AlreadyLoggedInException;
import shared.interfaces.AuthService;

public class LoginController {
    private final LoginView loginView; // The associated view for the login page
    private final LoginModel loginModel; // The associated model for the login page
    private final AuthService authService; // Authentication service

    public LoginController(LoginView loginView, LoginModel loginModel, AuthService authService) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        this.authService = authService;

        this.loginView.setActionSignInButton(this::handleSignIn);
        this.loginView.setActionSignUpButton(this::redirectToSignUp);
    }

    private void handleSignIn(ActionEvent event) {
        String email = loginView.getIdField().getText().trim();
        String password = loginView.getPassField().getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            loginView.setPromptLabel("Please complete all fields.");
            System.out.println("test");
            loginView.setPromptLabelVisible(true);
            return;
        }

        try {
            // Attempt to log in the user
            User user = authService.login(email, password);

            if (user != null) {
                System.out.println("[INFO] Login successful for user: " + user.getEmail());

                // Redirect to the main menu based on user role
                redirectToMainMenu(user);
            } else {
                loginView.setPromptLabel("Invalid credentials. Please try again.");
                loginView.setPromptLabelVisible(true);
            }
        } catch (AlreadyLoggedInException e) {
            System.err.println("[AUTH FAILED] Account was logged in elsewhere, but you are now logged in.");
            loginView.setPromptLabel("Account was logged in elsewhere. You are now logged in.");
            loginView.setPromptLabelVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "An error occurred during login. Please try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redirectToMainMenu(User user) {
        // Logic to redirect to the main menu based on user role
        if ("Student".equalsIgnoreCase(user.getRole())) {
            // Redirect to Admin Main Menu
        } else {
            // Redirect to User Main Menu
        }
    }

    private void redirectToSignUp(ActionEvent event) {
        // Logic to redirect to the sign-up page
    }
}
