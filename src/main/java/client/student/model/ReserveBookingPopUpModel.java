package client.student.model;

import shared.classes.Booking;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

public class ReserveBookingPopUpModel {
    private final StudentService studentService;

    public ReserveBookingPopUpModel(StudentService studentService) {
        this.studentService = studentService;
    }

    public Booking createBooking(String studentId, TutorSession session) throws Exception {
        try {
            return studentService.createBooking(
                    studentId,
                    session.getSessionID(),
                    session.getSessionMode(),
                    "Approved",
                    session.getSessionPrice()
            );
        } catch (Exception e) {
            throw new Exception("Failed to create booking: " + e.getMessage());
        }
    }

    public void processPayment(String studentId, double amount, String paymentMethod) throws Exception {
        try {
            System.out.println("[DEBUG][Model] Processing payment for student: " + studentId);
            studentService.createPayment(studentId, amount, paymentMethod);
        } catch (Exception e) {
            throw new Exception("Payment failed: " + e.getMessage());
        }
    }
}