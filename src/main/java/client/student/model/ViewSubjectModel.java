package client.student.model;

import shared.classes.Subject;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewSubjectModel {
    private final StudentService service;

    public ViewSubjectModel(StudentService service) {
        this.service = service;
    }

    public List<Subject> fetchSubjects() throws RemoteException {
        // Fetch subjects from the service
        return service.viewSubject();
    }
}