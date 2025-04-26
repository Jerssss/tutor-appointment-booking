package client.admin.model;

import shared.interfaces.AdminService;
import client.AdminClient;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminModifySubjectPopUpModel {

    private final AdminService adminService;

    public AdminModifySubjectPopUpModel() {
        this.adminService = AdminClient.getAdminProcessors();
    }
    public void updateSubject(String subjectID, String academicLevel) throws RemoteException, SQLException {
        adminService.modifySubject(subjectID, academicLevel);
    }
}
