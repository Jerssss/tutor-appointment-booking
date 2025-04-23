package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminDeleteSessionPopUpModel {
    private final AdminService adminService;

    public AdminDeleteSessionPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void deleteSession(String sessionID) throws RemoteException {
        adminService.deleteSession(sessionID);
    }
}
