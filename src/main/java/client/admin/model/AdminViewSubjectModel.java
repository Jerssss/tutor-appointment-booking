package client.admin.model;

import client.AdminClient;
import shared.classes.Subject;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSubjectModel {

    private final AdminService adminService;

    public AdminViewSubjectModel( ){
        this.adminService = AdminClient.getAdminService();
    }

    public List<Subject> displaySubjects() throws RemoteException {
        return adminService.viewSubject();
    }
}
