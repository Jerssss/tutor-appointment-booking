package client.landingpage.login;

import client.student.controller.StudentMainMenuController;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.exceptions.AlreadyLoggedInException;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;

import java.io.IOException;

import static client.AdminClient.getAuthService;


public class LoginController {
    private final LoginView loginView;
    private final LoginModel loginModel;
    private final AuthService authService;
    private final StudentService studentService;

    public LoginController(LoginView loginView, LoginModel loginModel,
                           AuthService authService, StudentService studentService) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        this.authService = authService;
        this.studentService = studentService;

        this.loginView.setActionSignInButton(this::handleSignIn);
        this.loginView.setActionSignUpButton(this::redirectToSignUp);
    }

    private void handleSignIn(ActionEvent event) {
        String userID = loginView.getIdField().getText().trim();
        String password = loginView.getPassField().getText().trim();

        if (userID.isEmpty() || password.isEmpty()) {
            loginView.setPromptLabel("Please complete all fields.");
            loginView.setPromptLabelVisible(true);
            return;
        }

        try {
            // Use the authService passed in the constructor, not AdminClient.getAuthService()
            User user = authService.login(userID, password);
            System.out.println("[INFO] Login successful for user: " + user.getUserID());
            redirectToMainMenu(event, user);

        } catch (AccountDoesNotExist e) {
            loginView.setPromptLabel("Invalid userID or password. Please try again.");
            loginView.setPromptLabelVisible(true);
        } catch (AlreadyLoggedInException e) {
            loginView.setPromptLabel("Account was logged in elsewhere. You are now logged in.");
            loginView.setPromptLabelVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            loginView.setPromptLabel("An error occurred. Please try again.");
            loginView.setPromptLabelVisible(true);
        }
    }




    private void redirectToMainMenu(ActionEvent event, User user) {
        if ("Student".equalsIgnoreCase(user.getRole())) {
            try {
                // Correct the resource path to point to the resources directory
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/student/student_menu_page.fxml"));
                Parent root = fxmlLoader.load();
                StudentMainMenuView studentMainMenuView = fxmlLoader.getController();

                String loggedInUserName = user.getFirstName() + " " + user.getLastName();
                new StudentMainMenuController(studentMainMenuView, new StudentMainMenuModel(studentService), loggedInUserName);

                changeScene(event, root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // TODO: Redirect other roles like Tutor if needed
        }
    }


    private void redirectToSignUp(ActionEvent event) {
        // Logic to redirect to the sign-up page
    }

    private void changeScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
