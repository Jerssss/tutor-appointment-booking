package client.admin.controller;

import client.admin.model.AdminPaymentModel;

import java.util.List;

public class AdminCreatePaymentController {

    private final AdminPaymentModel model;
    public AdminCreatePaymentController(){
        this.model = new AdminPaymentModel();
    }
    public List<String> getStudentList() {
        return model.getStudentList();
    }
}
