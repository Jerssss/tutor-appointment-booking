package client.student.controller;


import client.StudentTutorClient;
import client.landingpage.login.LoginController;
import client.landingpage.login.LoginModel;
import client.landingpage.login.LoginView;
import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.classes.SessionManager;
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;

import javax.swing.*;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.Date;

public class StudentMainMenuController {
    private final StudentMainMenuView view;
    private final StudentMainMenuModel model;
    private final String loggedInUserName;
    private Thread serverThread;

    public StudentMainMenuController(StudentMainMenuView view, StudentMainMenuModel model, String loggedInUserName) {

        //for security
        if (!"Student".equals(SessionManager.getCurrentUserRole())) {
            throw new IllegalStateException("Unauthorized access");
        }
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();
        this.view.setActionLogoutButton(this::handleLogout);

        this.view.setActionCreateBookingButton(event -> handleCreateReservation());
        this.view.setActionViewBookingButton(event -> handleViewReservation());
        this.view.setActionRescheduleBookingButton(event -> handleModifyReservation());
        this.view.setActionSubjectsOfferedButton(event -> handleSubjectsOffered());
        this.view.setActionLessonPlanButton(event -> handleLessonPlan());
        this.view.setActionPaymentHistoryButton(event -> handlePaymentHistory());
        this.view.setActionBalanceButtonEventHandler(event -> handleBalance());

    }
    private void handleCreateReservation() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Create Reservation");
    }

    private void handleViewReservation() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to View Reservation");
    }

    private void handleModifyReservation() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Modify Reservation");
    }

    private void handleSubjectsOffered() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Subjects Offered");
    }

    private void handleLessonPlan() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Lesson Plan");
    }

    private void handlePaymentHistory() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Payment History");
    }

    private void handleBalance() {
        System.out.println("[CLIENT | "+ new Date()+ "] Navigating to Balance");
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
