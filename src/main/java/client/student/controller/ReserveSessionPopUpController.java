package client.student.controller;

import shared.classes.Booking;
import shared.classes.Student;
import shared.classes.Tutor;
import shared.classes.TutorSession;
import client.student.model.ReserveSessionPopUpModel;

public class ReserveSessionPopUpController {
    private final ReserveSessionPopUpModel model;

    public ReserveSessionPopUpController(ReserveSessionPopUpModel model) {
        this.model = model;
    }

    public Student getStudentDetails(String studentId) throws Exception {
        return model.getStudentDetails(studentId);
    }

    public Tutor getTutorDetails(String tutorId) throws Exception {
        return model.getTutorDetails(tutorId);
    }

    public void processBooking(String studentId, TutorSession session,
                               boolean payNow, String paymentMethod,
                               double amountPaid) throws Exception {
        model.processBooking(studentId, session, payNow, paymentMethod, amountPaid);
    }
}