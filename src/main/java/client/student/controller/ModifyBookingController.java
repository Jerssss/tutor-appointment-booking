package client.student.controller;

import client.student.model.ModifyBookingModel;
import client.student.view.ModifyBookingView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Booking;
import shared.classes.BookingDetails;
import shared.classes.SessionManager;

import java.rmi.RemoteException;
import java.util.List;

public class ModifyBookingController {
    private final ModifyBookingModel model;
    private final ModifyBookingView view;

    public ModifyBookingController(ModifyBookingModel model, ModifyBookingView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this); // Set the controller in the view
    }

    public void refreshTable() {
        try {
            // Get student ID from session
            String studentIdStr = SessionManager.getCurrentUserId();
            if (studentIdStr == null) {
                view.showErrorAlert("Session Error", "No active session found");
                return;
            }

            // Parse to integer (assuming your student IDs are numeric)
            int studentId;
            try {
                studentId = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException e) {
                view.showErrorAlert("Invalid ID", "Student ID must be numeric");
                return;
            }

            // Fetch and display bookings
            List<BookingDetails> bookings = model.fetchBookings(studentId);
            ObservableList<BookingDetails> observableBookings = FXCollections.observableArrayList(bookings);
            view.updateTable(observableBookings);

        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error", "Failed to load bookings: " + e.getMessage());
        }
    }

    public void modifyBooking(String studentID, String sessionID, String newSessionMode, String newSessionDate, String newSessionTime, String newBookingStatus, double newSessionPrice) {
        try {
            Booking updatedBooking = model.modifyBooking(studentID, sessionID, newSessionMode, newBookingStatus, newSessionPrice, newSessionDate, newSessionTime);
            if (updatedBooking != null) {
                System.out.println("[CLIENT] Booking modified successfully.");
                refreshTable(); // Refresh the table to show updated bookings
            } else {
                System.out.println("Failed to modify booking.");
            }
        } catch (RemoteException e) {
            System.err.println("Error modifying booking: " + e.getMessage());
        }
    }

    public void cancelBooking(String sessionID) {
        try {
            // Call the model to cancel the booking
            boolean success = model.cancelBooking(sessionID);
            if (success) {
                System.out.println("[CLIENT] Booking cancelled successfully.");
                refreshTable(); // Refresh the table to reflect the cancellation
            } else {
                view.showErrorAlert("Cancellation Failed", "Failed to cancel the booking.");
            }
        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error", "Failed to cancel the booking: " + e.getMessage());
        }
    }
}