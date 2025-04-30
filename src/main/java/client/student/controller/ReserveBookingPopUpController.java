package client.student.controller;

import client.student.model.ReserveBookingPopUpModel;
import shared.classes.Booking;
import shared.classes.Tutor;
import shared.classes.TutorSession;

import java.rmi.RemoteException;

public class ReserveBookingPopUpController {
    private final ReserveBookingPopUpModel model;

    public ReserveBookingPopUpController(ReserveBookingPopUpModel model) {
        this.model = model;
    }

    public Tutor getTutorDetails(String tutorId) throws Exception {
        try {
            return model.getTutorDetails(tutorId);
        } catch (Exception e) {
            throw new Exception("Failed to fetch tutor details: " + e.getMessage());
        }
    }
    public void processBooking(String studentId, TutorSession session,
                               boolean payNow, String paymentMethod,
                               double amountPaid) throws Exception {
        // Always create the booking first
        Booking booking = model.createBooking(studentId, session);

        if (payNow) {
            System.out.println("Processing immediate payment of " + amountPaid);
            try {
                // Process the partial payment
                model.processPayment(studentId, amountPaid, paymentMethod);

                // Calculate and update the remaining balance
                double remainingBalance = session.getSessionPrice() - amountPaid;
                if (remainingBalance > 0) {
                    model.updateStudentBalance(studentId, remainingBalance);
                }
                System.out.println("Payment succeeded");
            } catch (Exception e) {
                System.out.println("Payment failed: " + e.getMessage());
                throw e;
            }
        } else {
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