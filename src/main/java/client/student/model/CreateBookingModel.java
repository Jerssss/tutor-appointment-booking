package client.student.model;

import client.StudentTutorClient;
import shared.classes.*;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingModel {
    private final StudentService studentService;

    public CreateBookingModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public List<TutorSession> fetchSessions() throws RemoteException {
        return studentService.viewAvailableSessions();
    }

    public Booking createBooking(String studentId, String sessionId,
                                 String sessionMode, String status, double price)
            throws RemoteException {
        return studentService.createBooking(
                studentId,
                sessionId,
                sessionMode,
                status,
                price
        );
    }

    public Payment createPayment(String studentId, double amount,
                                 String paymentMethod) throws RemoteException {
        return studentService.createPayment(studentId, amount, paymentMethod);
    }

    public void updateStudentBalance(String studentId, double amount)
            throws RemoteException {
        studentService.updateStudentBalance(studentId, amount);
    }
}