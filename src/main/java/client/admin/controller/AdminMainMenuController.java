package client.admin.controller;

import client.admin.model.AdminMainMenuModel;
import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminMainMenuView;
import client.admin.view.AdminViewSessionView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMainMenuController {
    private final AdminMainMenuView view;
    private final AdminMainMenuModel model;
    private final String loggedInUserName;
    private Thread serverThread;

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

       // this.view.setActionLogoutButton(this::handleLogout);

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

    private void changeScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
