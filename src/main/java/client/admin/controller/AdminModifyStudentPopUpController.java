package client.admin.controller;

import client.admin.model.AdminStudentModel;

public class AdminModifyStudentPopUpController {
    private final AdminStudentModel model;
    public AdminModifyStudentPopUpController(){
        this.model = new AdminStudentModel();
    }

    public boolean modifyStudent(String id, String newPassword) {
        return model.modifyStudentPassword(id, newPassword);
    }
}
