package client.admin.model;

import shared.classes.Tutor;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSessionModel {
    private final AdminService adminService;

    public AdminViewSessionModel(AdminService adminService){
        this.adminService = adminService;
    }

    public List<List<String>> displaySessions() throws RemoteException {
        return adminService.viewSession();
    }
}
