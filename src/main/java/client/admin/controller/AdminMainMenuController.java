package client.admin.controller;

import client.StudentTutorClient;
import client.admin.model.AdminMainMenuModel;
import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminMainMenuView;
import client.admin.view.AdminViewSessionView;
import client.landingpage.login.*;
import client.landingpage.signup.SignUpView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class AdminMainMenuController {
    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;
    private final String loggedInUserName;
    private Thread serverThread;
    private static AuthService authService;
    private static AdminService adminService;

    public static AuthService getAuthService() {
        return authService;
    }

    public static AdminService getAdminService() {
        return adminService;
    }

    public AdminMainMenuController(AdminMainMenuView view, AdminMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();
        this.view.setActionStudentButton(event -> handleStudentButton());
        this.view.setActionTutorButton(event -> handleTutorButton());
        this.view.setActionSessionsButton(event -> handleSessionButton(event));
        this.view.setActionSubjectButton(event -> handleSubjectButton());
        this.view.setActionLessonPlanButton(event -> handleLessonPlanButton());
        this.view.setActionPaymentButton(event -> handlePaymentButton());

        this.view.setActionLogoutButton(this::handleLogout);

    }
    private void handleStudentButton() {
        System.out.println("Navigating to Student...");
    }
    private void handleTutorButton() {
        System.out.println("Navigating to Tutor...");
    }
    private void handleSessionButton(ActionEvent event) {
        redirectToSessions(event);
    }
    private void handleSubjectButton() {
        System.out.println("Navigating to Subject...");
    }
    private void handleLessonPlanButton() {
        System.out.println("Navigating to Lesson Plan...");
    }
    private void handlePaymentButton() {
        System.out.println("Navigating to Payment History...");
    }

    private void redirectToSessions(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/sessions_pane.fxml"));
            Parent root = fxmlLoader.load();

            AdminViewSessionController sessionController = fxmlLoader.getController();
            sessionController.displaySessions();
            changeScene(event, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogout(ActionEvent event){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_login_page.fxml"));
                Parent root = loader.load();

                AdminLoginView adminLoginView = loader.getController();
                if (adminLoginView == null) {
                    System.err.println("[ERROR] AdminLoginView controller is NULL after FXML load!");
                    return;
                }

                // Initialize model and controller for login
                AdminLoginModel adminLoginModel = new AdminLoginModel(authService);
                new AdminLoginController(adminLoginView, adminLoginModel, authService, adminService);

                // Change to the login scene
                changeScene(event, root);

                System.out.println("[INFO] Logged out and returned to Admin Login Page.");
            } catch (IOException e) {
                System.err.println("[ERROR] Failed to load admin_login_page.fxml: " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
            }
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
