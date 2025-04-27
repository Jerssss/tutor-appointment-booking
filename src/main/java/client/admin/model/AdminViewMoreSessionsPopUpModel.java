package client.admin.model;

import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewMoreSessionsPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSessionsPopUpModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<String> getDetails(String sessionID) throws RemoteException {
        return adminService.viewOtherSessionDetails(sessionID);
    }

}
