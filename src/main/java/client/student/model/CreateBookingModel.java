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

    public void setCurrentStudentId(int studentId) {
        this.currentStudentId = studentId;
    }

    public List<TutorSession> getAvailableSessions() throws RemoteException {
        // In a real implementation, you would call a service method
        // For now, return empty list - need to add this method to StudentService
        return List.of();
    }

    public Booking createBooking(int sessionId) throws RemoteException {
        return studentService.createBooking(
                currentStudentId,
                sessionId,
                "Online", // Default session mode
                "Pending", // Default status
                0.0 // Default price (should get from session)
        );
    }

    public boolean refreshAvailableSessions() throws RemoteException {
        // Implementation would refresh the available sessions
        return true;
    }
}