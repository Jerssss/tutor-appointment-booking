package client.student.model;

import shared.classes.BalanceDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewStudentBalanceModel {
    private final StudentService service;

    public ViewStudentBalanceModel(StudentService service) {
        this.service = service;
    }

    public List<BalanceDetails> fetchBalanceDetails(String studentID) throws RemoteException {
        return service.viewStudentBalanceDetails(studentID);
    }

    public double fetchStudentBalance(String studentID) throws RemoteException {
        return service.getStudentBalance(studentID);
    }
}