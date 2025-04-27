package client.admin.model;


import shared.classes.TutorSession;
import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminModifySessionPopUpModel {

    private final AdminService adminService;

    public AdminModifySessionPopUpModel() {
        this.adminService = AdminClient.getAdminService();
    }
    public void updateSession(String sessionID, String sessionMode, String sessionType, String numStudents, String maxStudents, String sessionPrice, String sessionStatus, String sessionVisibility) throws RemoteException, SQLException {
        adminService.modifySession(sessionID, sessionMode, sessionType, numStudents, maxStudents, sessionPrice, sessionStatus, sessionVisibility);
    }

    public TutorSession getEditableDetails(String sessionID) throws RemoteException {
        return adminService.getEditableDetails(sessionID);
    }
}
