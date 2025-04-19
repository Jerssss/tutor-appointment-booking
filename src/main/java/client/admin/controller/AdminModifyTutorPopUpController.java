package client.admin.controller;

import client.admin.model.AdminTutorModel;

public class AdminModifyTutorPopUpController {
    private final AdminTutorModel model;
    public AdminModifyTutorPopUpController(){
        this.model = new AdminTutorModel();
    }

    public boolean modifyTutor(String id, String newPassword) {
        return model.modifyTutorPassword(id, newPassword);
    }
}
