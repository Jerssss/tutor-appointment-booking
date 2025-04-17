package client.admin.model;

import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminViewMoreSessionsPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreSessionsPopUpModel(AdminService adminService){
        this.adminService = adminService;
    }

    public List<String> getDetails(String sessionID) throws RemoteException {
        return adminService.viewOtherSessionDetails(sessionID);
    }

}
