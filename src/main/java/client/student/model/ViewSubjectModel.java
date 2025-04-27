package client.student.model;

import client.StudentTutorClient;
import shared.classes.Subject;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewSubjectModel {
    private final StudentService studentService;

    public ViewSubjectModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public List<Subject> fetchSubjects() throws RemoteException {
        // Fetch subjects from the service
        return studentService.viewSubject();
    }
}