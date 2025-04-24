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
    public int deleteSession(String sessionID) throws RemoteException {
        return adminService.deleteSession(sessionID);
    }
}
