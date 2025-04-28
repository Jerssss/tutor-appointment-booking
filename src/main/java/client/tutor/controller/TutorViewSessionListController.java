package client.tutor.controller;

import client.tutor.model.TutorViewSessionListModel;
import client.tutor.view.TutorViewSessionListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.TutorSession;
import shared.classes.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public void loadSessionData() {
        // Get tutor ID from session
        String tutorID = SessionManager.getCurrentUserId();
        System.out.println("Current Tutor ID: " + tutorID);
        if (tutorID == null) {
            System.err.println("[ERROR] No active session found for tutor.");
            view.showErrorAlert("Session Error", "No active session found for tutor.");
            return;
        }

        List<TutorSession> sessions = model.getSessionList(tutorID);
        if (sessions != null) {
            Platform.runLater(() -> {
                sessionData.setAll(sessions);
                view.updateTable(sessions);
            });
        } else {
            System.out.println("error");
        }
    }

    public void searchSession(String query) {
        if (sessionData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(new ArrayList<>(sessionData)); // Reset table to original data
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<TutorSession> filteredList = sessionData.stream()
                .filter(session ->
                        safeLower(session.getSessionID()).contains(lowerCaseQuery) ||
                                safeLower(session.getSessionMode()).contains(lowerCaseQuery) ||
                                safeLower(session.getSessionDate().toString()).contains(lowerCaseQuery) || // Convert LocalDate to String
                                safeLower(session.getSubjectName()).contains(lowerCaseQuery) ||
                                safeLower(session.getSessionStatus()).contains(lowerCaseQuery) ||
                                safeLower(session.getSessionTime().toString()).contains(lowerCaseQuery) || // Convert LocalTime to String
                                safeLower(String.valueOf(session.getSessionDuration())).contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    // Helper method
    private String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
}