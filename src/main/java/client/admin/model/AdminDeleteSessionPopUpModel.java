package client.admin.model;

import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;


public class AdminDeleteSessionPopUpModel {
    private final AdminService adminService;

    public AdminDeleteSessionPopUpModel() {
        this.adminService = AdminClient.getAdminProcessors();
    }

    public int deleteSession(String sessionID) throws RemoteException {
        return adminService.deleteSession(sessionID);
    }
}
