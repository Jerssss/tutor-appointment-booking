package client.student.controller;

import client.student.model.CreateBookingModel;
import client.student.view.CreateBookingView;
import shared.classes.Booking;
import shared.classes.SessionManager;
import shared.classes.TutorSession;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingController {
    private CreateBookingModel model;
    private CreateBookingView view;

    public CreateBookingController(CreateBookingModel model, CreateBookingView view) {
        this.model = model;
        this.view = view;
    }

    public void refreshTable() {
        System.out.println("[CLIENT] Refreshing table...");
        try {
            // Fetch subjects from the model
            List<TutorSession> sessions = model.fetchSessions();
            System.out.println("[CLIENT] Fetched " + sessions.size() + " subjects.");
            // Update the view with the fetched subjects
            view.updateTable(sessions);
        } catch (RemoteException e) {
            System.err.println("Error fetching subjects: " + e.getMessage());
        }
    }

    public Booking createBooking(String studentId, String sessionId,
                                 String sessionMode, String status, double price)
            throws RemoteException {
        // Verify session ownership
        if (!studentId.equals(SessionManager.getCurrentUserId())) {
            throw new RemoteException("Session expired");
        }

        return model.createBooking(studentId, sessionId, sessionMode, status, price);
    }
}