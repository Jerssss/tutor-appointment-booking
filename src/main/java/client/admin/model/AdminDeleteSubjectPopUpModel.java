package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AdminDeleteSubjectPopUpModel {
    private final AdminService adminService;

    public AdminDeleteSubjectPopUpModel() {
        this.adminService = AdminClient.getAdminService();
    }

    public int deleteSubject(Subject subject) throws RemoteException, SQLIntegrityConstraintViolationException {
        return adminService.deleteSubject(subject);
    }
}
