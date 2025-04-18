package client.admin.model;

import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminViewMoreSubjectPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSubjectPopUpModel(AdminService adminService){
        this.adminService = adminService;
    }

    public List<String> getDetails(String subject) throws RemoteException {
        return adminService.viewOtherSubjectDetails(subject);
    }

}
