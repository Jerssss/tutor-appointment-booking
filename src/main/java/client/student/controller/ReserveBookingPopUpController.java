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

        System.out.println("ID Hex Debug: " + bytesToHex(studentId.getBytes()));

        Booking booking = model.createBooking(studentId, session);


        if (payNow) {
            System.out.println("Attempting payment for: " + studentId); // Debug
            try {
                model.processPayment(studentId, session.getSessionPrice(), paymentMethod);
                System.out.println("Payment succeeded"); // Debug
            } catch (Exception e) {
                System.out.println("Payment failed: " + e.getMessage()); // Debug
                throw e; // Re-throw to show error in UI
            }
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}