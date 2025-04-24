package client.student.controller;

import client.student.model.ReserveBookingPopUpModel;
import shared.classes.Booking;
import shared.classes.TutorSession;

public class ReserveBookingPopUpController {
    private final ReserveBookingPopUpModel model;

    public ReserveBookingPopUpController(ReserveBookingPopUpModel model) {
        this.model = model;
    }

    public void processBooking(String studentId, TutorSession session,
                               boolean payNow, String paymentMethod) throws Exception {
        // Always create the booking first
        Booking booking = model.createBooking(studentId, session);

        if (payNow) {
            // If paying now, just process the payment (don't adjust balance)
            System.out.println("Processing immediate payment");
            try {
                model.processPayment(studentId, session.getSessionPrice(), paymentMethod);
                System.out.println("Payment succeeded");
            } catch (Exception e) {
                System.out.println("Payment failed: " + e.getMessage());
                throw e;
            }
        } else {
            // If paying later, update the student's balance (add the session price)
            System.out.println("Updating balance for later payment");
            try {
                model.updateStudentBalance(studentId, session.getSessionPrice());
                System.out.println("Balance updated for later payment");
            } catch (Exception e) {
                System.out.println("Balance update failed: " + e.getMessage());
                throw e;
            }
        }
    }
}