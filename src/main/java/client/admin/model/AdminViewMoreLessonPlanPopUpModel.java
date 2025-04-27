package client.admin.model;

import shared.interfaces.AdminService;
import client.AdminClient;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewMoreLessonPlanPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreLessonPlanPopUpModel( ){
        this.adminService = AdminClient.getAdminService();
    }


    public List<String> getDetails(String subject) throws RemoteException {
        return adminService.viewOtherLessonPlanDetails(subject);
    }

}
