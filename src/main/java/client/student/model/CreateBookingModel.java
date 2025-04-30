package client.student.model;

import client.StudentTutorClient;
import shared.classes.*;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.util.List;
import shared.classes.SessionManager;


public class CreateBookingModel {
    private final StudentService studentService;


    public CreateBookingModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public List<TutorSession> fetchSessions() throws RemoteException {
        return studentService.viewAvailableSessions(SessionManager.getCurrentUserId());
    }

}