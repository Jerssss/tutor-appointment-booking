package client.student.controller;

import client.student.model.CreateBookingModel;
import client.student.view.CreateBookingView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shared.classes.Booking;
import shared.classes.TutorSession;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingController {
    private CreateBookingModel model;
    private CreateBookingView view;

    public CreateBookingController(CreateBookingModel model, CreateBookingView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    private void initialize() {
        setupEventHandlers();
        loadAvailableSessions();
    }

    private void setupEventHandlers() {
        view.getRefreshButton().setOnAction(event -> {
            try {
                if (model.refreshAvailableSessions()) {
                    loadAvailableSessions();
                }
            } catch (RemoteException e) {
                showError("Connection Error", "Failed to refresh sessions");
            }
        });
    }

    private void loadAvailableSessions() {
        try {
            List<TutorSession> sessions = model.getAvailableSessions();
            ObservableList<TutorSession> sessionData = FXCollections.observableArrayList(sessions);
            view.getCreateReservationTableView().setItems(sessionData);
        } catch (RemoteException e) {
            showError("Connection Error", "Failed to load available sessions");
        }
    }

    public void handleReserveAction(TutorSession selectedSession) {
        try {
            Booking newBooking = model.createBooking(selectedSession.getSessionID());
            showConfirmation("Booking Created",
                    "Successfully booked session on " + selectedSession.getSessionDate());
        } catch (RemoteException e) {
            showError("Booking Error", "Failed to create booking");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}