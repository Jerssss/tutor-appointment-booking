package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminAddSubjectPopUpModel {
    private final AdminService adminService;

    public AdminAddSubjectPopUpModel(){
        this.adminService = AdminClient.getAdminService();
    }

    public void addNewSubject(String subjectID, String subjectName, String subjectDescription, String academicLevel) throws RemoteException, SQLException {
        adminService.addSubject(new Subject(subjectID, subjectName, subjectDescription, academicLevel));
    }

}
