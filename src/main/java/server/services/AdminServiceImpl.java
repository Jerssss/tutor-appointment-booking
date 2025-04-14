package server.services;

import shared.classes.*;
import shared.interfaces.AdminService;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.List;

public class AdminServiceImpl implements Remote, AdminService, Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID
    @Override
    public List<Student> viewStudent() {
        return null;
    }

    @Override
    public Student addStudent() {
        return null;
    }

    @Override
    public Student modifyStudent() {
        return null;
    }

    @Override
    public Tutor viewTutor() {
        return null;
    }

    @Override
    public Tutor addTutor() {
        return null;
    }

    @Override
    public List<TutorSession> viewSession() {
        return null;
    }

    @Override
    public TutorSession addSession() {
        return null;
    }

    @Override
    public TutorSession modifySession() {
        return null;
    }

    @Override
    public List<Subject> viewSubject() {
        return null;
    }

    @Override
    public Subject addSubject() {
        return null;
    }

    @Override
    public Subject modifySubject() {
        return null;
    }

    @Override
    public List<LessonPlan> viewLessonPlan() {
        return null;
    }

    @Override
    public LessonPlan addLessonPlan() {
        return null;
    }

    @Override
    public LessonPlan modifyLessonPlan() {
        return null;
    }

    @Override
    public List<Payment> viewPayment() {
        return null;
    }
}
