package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.Student;
import shared.interfaces.TutorService;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;

public class TutorViewStudentListPopUpModel {
    private final TutorService tutorService;


    public TutorViewStudentListPopUpModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public List<Student> fetchStudents(String sessionID) {
        try {
            return tutorService.getStudentsBySession(sessionID);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch sessions: " + e.getMessage());
            return null;
        }
    }

    public List<Student> getStudentsBySession(String sessionID) {
        try {
            return tutorService.getStudentsBySession(sessionID);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to retrieve students for session ID: " + sessionID);
            return Collections.emptyList();
        }
    }
}
