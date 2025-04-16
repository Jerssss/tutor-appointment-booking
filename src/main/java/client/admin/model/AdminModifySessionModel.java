package client.admin.model;

import shared.classes.Subject;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminModifySessionModel {

    private final AdminService adminService;

    public AdminModifySessionModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSession(String sessionID, String sessionMode, String sessionType) throws RemoteException, SQLException {
        adminService.modifySession(sessionID, sessionMode, sessionType);
    }
}
