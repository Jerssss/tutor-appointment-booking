package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminModifySessionPopUpModel {

    private final AdminService adminService;

    public AdminModifySessionPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSession(String sessionID, String sessionMode, String sessionType, String numStudents, String maxStudents, String sessionPrice) throws RemoteException, SQLException {
        adminService.modifySession(sessionID, sessionMode, sessionType, numStudents, maxStudents, sessionPrice);
    }

    public List<String> getEditableDetails(String sessionID) throws RemoteException {
        return adminService.getEditableDetails(sessionID);
    }
}
