package client.admin.model;

import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewMoreSubjectPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSubjectPopUpModel( ){
        this.adminService = AdminClient.getAdminProcessors();
    }

    public List<String> getDetails(String subject) throws RemoteException {
        return adminService.viewOtherSubjectDetails(subject);
    }

}
