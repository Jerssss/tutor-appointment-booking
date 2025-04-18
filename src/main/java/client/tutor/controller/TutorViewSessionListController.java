package client.tutor.controller;

import client.tutor.model.TutorViewSessionListModel;
import client.tutor.view.TutorViewSessionListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import shared.classes.TutorSession;
import java.util.List;


public class TutorViewSessionListController {
    private final TutorViewSessionListView view;
    private final TutorViewSessionListModel model;
    private ObservableList<TutorSession> sessionData = FXCollections.observableArrayList();


    public TutorViewSessionListController(TutorViewSessionListView view) {
        this.view = view;
        this.model = new TutorViewSessionListModel();


        // Load session data when the controller is initialized
        loadSessionData();
    }


    private void loadSessionData() {
        List<TutorSession> sessions = model.getSessionList();
        if (sessions != null) {
            Platform.runLater(() -> {
                sessionData.setAll(sessions);
                view.updateTable(sessions);
            });
        } else {
            System.out.println("error");
        }
    }


    @FXML
    private void handleViewStudent(ActionEvent event) {
        // Functionality to be implemented later
    }
}
