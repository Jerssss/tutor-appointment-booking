package client.student.model;

import shared.classes.Booking;
import shared.classes.Student;
import shared.classes.Tutor;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

public class ReserveSessionPopUpModel {
    private final StudentService studentService;

    public ReserveSessionPopUpModel(StudentService studentService) {
        this.studentService = studentService;
    }

    public Student getStudentDetails(String studentId) throws Exception {
        try {
            return studentService.getStudent(studentId);
        } catch (Exception e) {
            throw new Exception("Could not retrieve student details: " + e.getMessage());
        }
    }

    public Tutor getTutorDetails(String tutorId) throws Exception {
        try {
            return studentService.getTutorDetails(tutorId);
        } catch (Exception e) {
            throw new Exception("Could not retrieve tutor details: " + e.getMessage());
        }
    }

    public void processBooking(String studentId, TutorSession session,
                               boolean payNow, String paymentMethod,
                               double amountPaid) throws Exception {
        try {
            Booking booking = studentService.createBooking(studentId, session.getSessionID(),
                    session.getSessionMode(), "Pending", session.getSessionPrice());

            if (payNow) {
                studentService.createPayment(studentId, amountPaid, paymentMethod);
            }
        } catch (Exception e) {
            throw new Exception("Booking processing failed: " + e.getMessage());
        }
    }
}