package client.admin.model;

import client.AdminClient;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewSessionModel {
    private final AdminService adminService;

    public AdminViewSessionModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<TutorSession> displayAllSessions() throws RemoteException {
        return adminService.viewSession();
    }
    public List<TutorSession> displaySessions() throws RemoteException {
        List<TutorSession> temp = adminService.viewSession();
        List<TutorSession> sessions = new ArrayList<>();
        for (TutorSession session: temp){
            if (session.getVisibility().equals("Available")){
                sessions.add(session);
            }
        }
        return sessions;
    }

    public List<TutorSession> displayArchivedSessions() throws RemoteException {
        List<TutorSession> temp = adminService.viewSession();
        List<TutorSession> sessions = new ArrayList<>();
        for (TutorSession session: temp){
            if (session.getVisibility().equals("Archived")){
                sessions.add(session);
            }
        }
        return sessions;
    }
}
