package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminModifySessionPopUpModel {

    private final AdminService adminService;

    public AdminModifySessionPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSession(String sessionID, String sessionMode, String sessionType) throws RemoteException, SQLException {
        adminService.modifySession(sessionID, sessionMode, sessionType);
    }
}
