package client.student.model;

import shared.classes.*;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingModel {
    private final StudentService studentService;
    private int currentStudentId;

    public CreateBookingModel(StudentService studentService) {
        this.studentService = studentService;
    }

    public List<TutorSession> fetchSessions() throws RemoteException {
        return studentService.viewAvailableSessions();
    }

    public Booking createBooking(String studentId, String sessionId,
                                 String sessionMode, String status, double price)
            throws RemoteException {

        return studentService.createBooking(
                Integer.parseInt(studentId),
                sessionId,
                sessionMode,
                status,
                price
        );
    }
}