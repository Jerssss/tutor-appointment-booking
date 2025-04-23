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
            System.err.println("[ERROR] Failed to fetch students: " + e.getMessage());
            return null;
        }
    }
}
