package client.student.controller;


import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;

public class StudentMainMenuController {
    private final StudentMainMenuView view;
    private final StudentMainMenuModel model;
    private final String loggedInUserName;
    private Thread serverThread;

    public StudentMainMenuController(StudentMainMenuView view, StudentMainMenuModel model, String loggedInUserName) {
        this.view = view;
        this.model = model;
        this.loggedInUserName = loggedInUserName;

        this.view.setLoggedInUserName(loggedInUserName);
        this.view.initializeDateTime();
        // this.view.setActionLogoutButton(this::handleLogout);

    }
}
