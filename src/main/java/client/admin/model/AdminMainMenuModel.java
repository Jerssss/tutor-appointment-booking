package client.admin.model;

import client.AdminClient;
import shared.interfaces.AdminService;

public class AdminMainMenuModel {

    private AdminService adminService;

    public AdminMainMenuModel() {
        this.adminService = AdminClient.getAdminService();
    }
}