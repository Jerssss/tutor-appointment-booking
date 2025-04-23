package client.student.controller;

import client.student.model.CreateBookingModel;
import client.student.view.CreateBookingView;
import shared.classes.*;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingController {
    private final CreateBookingModel model;
    private final CreateBookingView view;

    public CreateBookingController(CreateBookingModel model, CreateBookingView view) {
        this.model = model;
        this.view = view;
    }

    public void refreshTable() throws RemoteException {
        List<TutorSession> sessions = model.fetchSessions();
        view.updateTable(sessions);
    }

    public Booking createBooking(String studentId, String sessionId,
                                 String sessionMode, double price)
            throws RemoteException {
        return model.createBooking(
                studentId,
                sessionId,
                sessionMode,
                "Approved",
                price
        );
    }

    public Payment processPayment(String studentId, double amount,
                                  String paymentMethod) throws RemoteException {
        // Create payment record
        Payment payment = model.createPayment(studentId, amount, paymentMethod);

        // Update student balance
        model.updateStudentBalance(studentId, -amount);

        return payment;
    }
}