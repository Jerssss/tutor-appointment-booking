package client.tutor.controller;

import client.student.model.StudentMainMenuModel;
import client.student.view.StudentMainMenuView;
import client.tutor.model.TutorMainMenuModel;
import client.tutor.view.TutorMainMenuView;

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
        // this.view.setActionLogoutButton(this::handleLogout);

    }
}
