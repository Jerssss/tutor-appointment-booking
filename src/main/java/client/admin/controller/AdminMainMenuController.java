package client.admin.controller;

import client.admin.model.AdminMainMenuModel;
import client.admin.view.AdminMainMenuView;

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
        this.view.setActionSessionsButton(event -> handleSessionButton());
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
    private void handleSessionButton() {
        System.out.println("Navigating to Session...");
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
}
