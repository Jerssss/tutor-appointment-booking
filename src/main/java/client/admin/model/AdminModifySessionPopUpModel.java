package client.admin.model;


import shared.classes.TutorSession;
import client.AdminClient;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminModifySessionPopUpModel {

    private final AdminService adminService;

    public AdminModifySessionPopUpModel() {
        this.adminService = AdminClient.getAdminService();
    }
    public void updateSession(TutorSession session) throws RemoteException, SQLException {
        adminService.modifySession(session);
    }

    public TutorSession getEditableDetails(String sessionID) throws RemoteException {
        TutorSession editableSession = new TutorSession();
        List<TutorSession> sessions = adminService.viewSession();
        for (TutorSession session : sessions){
            if (session.getSessionID().equals(sessionID)){
                editableSession.setSessionStatus(session.getSessionStatus());
                editableSession.setSessionType(session.getSessionType());
                editableSession.setSessionMode(session.getSessionMode());
                editableSession.setNumberOfStudents(session.getNumberOfStudents());
                editableSession.setMaximumStudents(session.getMaximumStudents());
                editableSession.setSessionPrice(session.getSessionPrice());
                editableSession.setVisibility(session.getVisibility());
            }
        }
        return editableSession;
    }
}
