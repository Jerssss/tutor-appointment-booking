package client.admin.model;

import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminModifySessionPopUpModel {

    private final AdminService adminService;

    public AdminModifySessionPopUpModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSession(String sessionID, String sessionMode, String sessionType, String numStudents, String maxStudents, String sessionPrice, String sessionStatus) throws RemoteException, SQLException {
        adminService.modifySession(sessionID, sessionMode, sessionType, numStudents, maxStudents, sessionPrice, sessionStatus);
    }

    public TutorSession getEditableDetails(String sessionID) throws RemoteException {
        return adminService.getEditableDetails(sessionID);
    }
}
