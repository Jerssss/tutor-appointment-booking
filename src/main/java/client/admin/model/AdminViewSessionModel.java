package client.admin.model;

import client.AdminClient;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSessionModel {
    private final AdminService adminService;

    public AdminViewSessionModel( ){
        this.adminService = AdminClient.getAdminProcessors();
    }

    public List<TutorSession> displaySessions() throws RemoteException {
        return adminService.viewSession();
    }
}
