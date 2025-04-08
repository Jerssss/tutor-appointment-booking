package server.services;

import shared.classes.*;
import shared.interfaces.StudentService;

import java.rmi.Remote;

public class StudentServiceImpl implements Remote, StudentService {
    @Override
    public Booking createBooking() {
        return null;
    }

    @Override
    public Booking viewStudentBooking() {
        return null;
    }

    @Override
    public Booking modifyBooking() {
        return null;
    }

    @Override
    public Subject viewSubject() {
        return null;
    }

    @Override
    public LessonPlan viewLessonPlan() {
        return null;
    }

    @Override
    public Payment viewPaymentHistory() {
        return null;
    }

    @Override
    public Student viewStudentBalance() {
        return null;
    }
}
