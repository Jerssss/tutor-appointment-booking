package client.admin.model;

import shared.interfaces.AdminService;

public class AdminMainMenuModel {

    private AdminService adminService;

    public AdminMainMenuModel(AdminService adminService) {
        this.adminService = adminService;
    }
}