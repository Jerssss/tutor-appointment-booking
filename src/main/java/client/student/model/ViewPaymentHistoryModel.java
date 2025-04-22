package client.student.model;

import shared.classes.PaymentDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewPaymentHistoryModel {
    private final StudentService service;

    public ViewPaymentHistoryModel(StudentService service) {
        this.service = service;
    }

    public List<PaymentDetails> fetchPaymentHistory(int studentID) throws RemoteException {
        System.out.println("[CLIENT] Fetching payment history for studentID: " + studentID);
        List<PaymentDetails> payments = service.viewPaymentHistory(studentID);
        System.out.println("[CLIENT] Fetched " + payments.size() + " payments.");
        return payments;
    }
}