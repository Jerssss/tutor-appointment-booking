package client.student.controller;

import client.student.model.ViewStudentBookingModel;
import client.student.view.ViewStudentBookingView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.BookingDetails;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.util.List;

public class ViewStudentBookingController {
    private final ViewStudentBookingModel model;
    private final ViewStudentBookingView view;

    public ViewStudentBookingController(ViewStudentBookingModel model,
                                        ViewStudentBookingView view) {
        this.view = view;
        this.model = model;
        initialize();
    }

    private void initialize() {
        view.initializeTableColumns();
        refreshTable();
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
            List<BookingDetails> bookings = model.getStudentBookings(studentId);
            ObservableList<BookingDetails> observableBookings =
                    FXCollections.observableArrayList(bookings);
            view.updateTable(observableBookings);

        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error",
                    "Failed to load bookings: " + e.getMessage());
        }
    }
}