package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class AdminDeleteSubjectPopUpModel {
    private final AdminService adminService;

    public AdminDeleteSubjectPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }

    public int deleteSubject(String subjectID) throws RemoteException, SQLIntegrityConstraintViolationException {
        return adminService.deleteSubject(subjectID);
    }
}
