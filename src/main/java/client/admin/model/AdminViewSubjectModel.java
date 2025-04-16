package client.admin.model;

import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSubjectModel {

    private final AdminService adminService;

    public AdminViewSubjectModel(AdminService adminService){
        this.adminService = adminService;
    }

    public List<Subject> displaySubjects() throws RemoteException {
        return adminService.viewSubject();
    }
}
