package client.student.model;

import client.StudentTutorClient;
import shared.classes.BalanceDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewStudentBalanceModel {
    private StudentService studentService;

    public ViewStudentBalanceModel(StudentService service) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public List<BalanceDetails> fetchBalanceDetails(String studentID) throws RemoteException {
        return studentService.viewStudentBalanceDetails(studentID);
    }

    public double fetchStudentBalance(String studentID) throws RemoteException {
        return studentService.getStudentBalance(studentID);
    }
    public double getCurrentBalance(String studentID) throws RemoteException {
        return studentService.getStudentBalance(studentID);
    }
}