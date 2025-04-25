package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminModifySubjectPopUpModel {

    private final AdminService adminService;

    public AdminModifySubjectPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSubject(String subjectID, String academicLevel) throws RemoteException, SQLException {
        adminService.modifySubject(subjectID, academicLevel);
    }
}
