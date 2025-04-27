package client.admin.model;

import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AdminDeleteSubjectPopUpModel {
    private final AdminService adminService;

    public AdminDeleteSubjectPopUpModel() {
        this.adminService = AdminClient.getAdminService();
    }

    public int deleteSubject(String subjectID) throws RemoteException, SQLIntegrityConstraintViolationException {
        return adminService.deleteSubject(subjectID);
    }
}
