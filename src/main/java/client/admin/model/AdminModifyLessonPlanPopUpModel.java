package client.admin.model;

import shared.interfaces.AdminService;
import client.AdminClient;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class AdminModifyLessonPlanPopUpModel {

    private final AdminService adminService;

    public AdminModifyLessonPlanPopUpModel() {
        this.adminService = AdminClient.getAdminService();
    }
    public void updateLessonPlan(String lessonPlanID, String lessonPlanVisibility) throws RemoteException, SQLException {
        adminService.modifyLessonPlan(lessonPlanID, lessonPlanVisibility);
    }
}
