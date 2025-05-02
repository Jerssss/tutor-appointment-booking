package client.tutor.controller;

import client.StudentTutorClient;
import client.landingpage.login.LoginController;
import client.landingpage.login.LoginModel;
import client.landingpage.login.LoginView;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.tutor.model.TutorMainMenuModel;
import client.tutor.view.TutorMainMenuView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;

import javax.swing.*;
import java.io.IOException;
import java.util.Date;

public class TutorMainMenuController {
    private final TutorMainMenuView view;
    private final TutorMainMenuModel model;
    private final String loggedInUserName;
    private Thread serverThread;

    public TutorMainMenuController(TutorMainMenuView view, TutorMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();
        this.view.setActionLogoutButton(this::handleLogout);

        this.view.setActionSessionsButton(event -> handleViewSessions());
        this.view.setActionLessonPlanButton(event -> handleViewLessonPlan() );

    }
    private void handleViewSessions() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to View Sessions...");
    }

    private void handleViewLessonPlan() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to View Lesson Plan...");
    }


    private void handleLogout(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/common/login_page.fxml"));
            Parent root = loader.load();

            LoginView loginView = loader.getController();
            if (loginView == null) {
                System.err.println("[CLIENT | "+ new Date()+ "] LoginView is NULL after loading FXML!");
                return;
            }

            // Get services from StudentTutorClient
            AuthService authService = StudentTutorClient.getAuthService();
            StudentService studentService = StudentTutorClient.getStudentService();
            TutorService tutorService = StudentTutorClient.getTutorService();

            if (authService == null || studentService == null) {
                showErrorDialog("Required services are not available");
                return;
            }

            new LoginController(
                    loginView,
                    new LoginModel(authService),
                    authService,
                    studentService,
                    tutorService
            );

            switchScene(event, root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showErrorDialog("Error loading the login page. Please try again.");
        }
    }

    private void switchScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }


    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }
}
