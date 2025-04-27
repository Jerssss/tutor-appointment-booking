package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewMoreSubjectPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSubjectPopUpModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<String> getDetails(String subjectID) throws RemoteException {
        List<String> subjectDetails = new ArrayList<>();
        List<Subject> subjects = new ArrayList<>();
        for (Subject subject : subjects){
            if (subject.getSubjectID().equals(subjectID)){
                subjectDetails.add(subject.getSubjectName());
                subjectDetails.add(subject.getSubjectDescription());
            }
        }
        return subjectDetails;
    }

}
