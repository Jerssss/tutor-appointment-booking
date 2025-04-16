package client.admin.model;

import shared.classes.Subject;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class AdminModifySubjectModel {

    private final AdminService adminService;

    public AdminModifySubjectModel(AdminService adminService) {
        this.adminService = adminService;
    }
    public void updateSession(String subjectID, String academicLevel) throws RemoteException, SQLException {
        adminService.modifySubject(subjectID, academicLevel);
    }
}
