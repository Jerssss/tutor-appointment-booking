package client.landingpage.login;

import client.student.controller.StudentMainMenuController;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.tutor.controller.TutorMainMenuController;
import client.tutor.model.TutorMainMenuModel;
import client.tutor.view.TutorMainMenuView;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.classes.SessionManager;
import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.exceptions.AlreadyLoggedInException;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;

import java.io.IOException;

public class LoginController {
    private final LoginView loginView;
    private final LoginModel loginModel;
    private final AuthService authService;
    private final StudentService studentService;
    private final TutorService tutorService;

    public LoginController(LoginView loginView, LoginModel loginModel,
                           AuthService authService, StudentService studentService, TutorService tutorService) {
        this.loginView = loginView;
        this.loginModel = loginModel;
        this.authService = authService;
        this.studentService = studentService;
        this.tutorService = tutorService;

        this.loginView.setActionSignInButton(this::handleSignIn);
    }

    private void handleSignIn(ActionEvent event) {
        String userID = loginView.getIdField().getText().trim();
        String password = loginView.getPassField().getText().trim();
        String selectedUserType = loginView.getUserTypeBox().getValue();

        if (userID.isEmpty() || password.isEmpty()) {
            loginView.setPromptLabel("Please complete all fields.");
            loginView.setPromptLabelVisible(true);
            return;
        }

        if (selectedUserType == null || selectedUserType.isEmpty()) {
            loginView.setPromptLabel("Please select a user type.");
            loginView.setPromptLabelVisible(true);
            return;
        }

        try {
            User user = authService.login(userID, password);
            System.out.println("[INFO] Login successful for user: " + user.getUserID());

            if ("Student".equalsIgnoreCase(selectedUserType)) {
                if (!"Student".equalsIgnoreCase(user.getRole())) {
                    loginView.setPromptLabel("User is not registered as a Student.");
                    loginView.setPromptLabelVisible(true);
                    return;
                }
            } else if ("Tutor".equalsIgnoreCase(selectedUserType)) {
                if (!"Tutor".equalsIgnoreCase(user.getRole())) {
                    loginView.setPromptLabel("User is not registered as a Tutor.");
                    loginView.setPromptLabelVisible(true);
                    return;
                }
            } else {
                loginView.setPromptLabel("Invalid user type selected.");
                loginView.setPromptLabelVisible(true);
                return;
            }

            SessionManager.createSession(user.getUserID(), user.getRole());
            redirectToMainMenu(event, user);

        } catch (AccountDoesNotExist e) {
            loginView.setPromptLabel("Invalid userID or password. Please try again.");
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
                System.out.println("[DEBUG] Current user ID: " + SessionManager.getCurrentUserId());
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
            try {
                // Correct the resource path to point to the resources directory
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/tutor/tutor_menu_page.fxml"));
                Parent root = fxmlLoader.load();
                TutorMainMenuView tutorMainMenuView = fxmlLoader.getController();

                String loggedInUserName = user.getFirstName() + " " + user.getLastName();
                new TutorMainMenuController(tutorMainMenuView, new TutorMainMenuModel(tutorService), loggedInUserName);

                changeScene(event, root);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
