package client.admin.model;

import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewMoreLessonPlanPopUpModel {
    private final AdminService adminService;

    public AdminViewMoreLessonPlanPopUpModel(AdminService adminService){
        this.adminService = adminService;
    }

    public List<String> getDetails(String subject) throws RemoteException {
        return adminService.viewOtherLessonPlanDetails(subject);
    }

}
