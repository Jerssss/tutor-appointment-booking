package client.student.model;

import client.StudentTutorClient;
import shared.classes.Booking;
import shared.classes.Tutor;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

public class ReserveBookingPopUpModel {
    private final StudentService studentService;

    public ReserveBookingPopUpModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public Tutor getTutorDetails(String tutorId) throws Exception {
        try {
            return studentService.getTutorDetails(tutorId);
        } catch (Exception e) {
            throw new Exception("Failed to get tutor details: " + e.getMessage());
        }
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

    public void processPartialPayment(String studentId, double amountPaid,
                                      double sessionPrice, String paymentMethod) throws Exception {
        try {
            studentService.createPayment(studentId, amountPaid, paymentMethod);

            double remainingBalance = sessionPrice - amountPaid;
            if (remainingBalance > 0) {
                studentService.updateStudentBalance(studentId, remainingBalance);
            } else if (remainingBalance < 0) {
                throw new Exception("Payment amount cannot exceed session price");
            }

        } catch (Exception e) {
            throw new Exception("Payment processing failed: " + e.getMessage());
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