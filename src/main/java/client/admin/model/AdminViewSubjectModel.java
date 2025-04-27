package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewSubjectModel {

    private final AdminService adminService;

    public AdminViewSubjectModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<Subject> displayAllSubjects() throws RemoteException {
        return adminService.viewSubject();
    }
    public List<Subject> displaySubjects() throws RemoteException {
        List<Subject> temp = adminService.viewSubject();
        List<Subject> subjects = new ArrayList<>();
        for (Subject subject: temp){
            if (subject.getVisibility().equals("Available")){
                subjects.add(subject);
            }
        }
        return subjects;
    }

    public List<Subject> displayArchivedSubjects() throws RemoteException {
        List<Subject> temp = adminService.viewSubject();
        List<Subject> subjects = new ArrayList<>();
        for (Subject subject: temp){
            if (subject.getVisibility().equals("Archived")){
                subjects.add(subject);
            }
        }
        return subjects;
    }
}
