package client.admin.model;

import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminAddSubjectPopUpModel {
    private final AdminService adminService;

    public AdminAddSubjectPopUpModel(AdminService adminService){
        this.adminService = adminService;
    }

    public void addNewSubject(String subjectID, String subjectName, String subjectDescription, String academicLevel) throws RemoteException, SQLException {
        adminService.addSubject(new Subject(subjectID, subjectName, subjectDescription, academicLevel));
    }

}
