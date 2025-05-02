package client.student.model;

import client.StudentTutorClient;
import shared.classes.PaymentDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class ViewPaymentHistoryModel {
    private StudentService studentService;

    public ViewPaymentHistoryModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }


    public List<PaymentDetails> fetchPaymentHistory(String studentID) throws RemoteException {
        System.out.println("[CLIENT | "+ new Date()+ "] Fetching payment history for studentID: " + studentID);
        List<PaymentDetails> payments = studentService.viewPaymentHistory(studentID);
        System.out.println("[CLIENT | "+ new Date()+ "] Fetched " + payments.size() + " payments.");
        return payments;
    }
}