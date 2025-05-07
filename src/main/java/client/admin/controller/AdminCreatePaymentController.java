package client.admin.controller;

import client.admin.model.AdminPaymentModel;
import client.admin.model.AdminStudentModel;
import shared.classes.Payment;
import shared.classes.Student;

import java.util.List;

public class AdminCreatePaymentController {

    private final AdminPaymentModel model;
    public AdminCreatePaymentController(){
        this.model = new AdminPaymentModel();
    }
    public List<String> getStudentList() {
        return model.getStudentList();
    }

    public boolean addNewPayment(String studID, String method, int amount) {
        Payment newPayment = new Payment(studID, method, amount);
        return model.addNewPayment(newPayment);
    }
}
