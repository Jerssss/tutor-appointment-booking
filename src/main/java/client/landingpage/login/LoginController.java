package client.landingpage.login;

import javafx.event.ActionEvent;

import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.exceptions.AlreadyLoggedInException;
import static client.StudentTutorClient.getAuthService;

public class LoginController {
    private final LoginView loginView; // The associated view for the login page
    private final LoginModel loginModel; // The associated model for the login page


    public LoginController(LoginView loginView, LoginModel loginModel) {
        this.loginView = loginView;
        this.loginModel = loginModel;

        this.loginView.setActionSignInButton(this::handleSignIn);
        this.loginView.setActionSignUpButton(this::redirectToSignUp);
    }

    private void handleSignIn(ActionEvent event) {
        String email = loginView.getIdField().getText().trim();
        String password = loginView.getPassField().getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            loginView.setPromptLabel("Please complete all fields.");
            loginView.setPromptLabelVisible(true);
            return;
        }

        try {
            User user = getAuthService().login(email, password);
            System.out.println("[INFO] Login successful for user: " + user.getEmail());
            redirectToMainMenu(user);

        } catch (AccountDoesNotExist e) {
            loginView.setPromptLabel("Invalid email or password. Please try again.");
            loginView.setPromptLabelVisible(true);
        } catch (AlreadyLoggedInException e) {
            loginView.setPromptLabel("Account was logged in elsewhere. You are now logged in.");
            loginView.setPromptLabelVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
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
