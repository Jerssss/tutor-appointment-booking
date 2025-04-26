package client.student.model;

import client.StudentTutorClient;
import shared.classes.Booking;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

public class ReserveBookingPopUpModel {
    private final StudentService studentService;

    public ReserveBookingPopUpModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
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
            studentService.createPayment(studentId, amount, paymentMethod);
        } catch (Exception e) {
            throw new Exception("Payment failed: " + e.getMessage());
        }
    }

    public void updateStudentBalance(String studentId, double amount) throws Exception {
        try {
            studentService.updateStudentBalance(studentId, amount);
        } catch (Exception e) {
            throw new Exception("Balance update failed: " + e.getMessage());
        }
    }
}