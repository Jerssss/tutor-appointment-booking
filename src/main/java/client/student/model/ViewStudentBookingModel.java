package client.student.model;

import shared.classes.BookingDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewStudentBookingModel {
    private final StudentService studentService;

    public ViewStudentBookingModel(StudentService studentService) {
        this.studentService = studentService;
    }

    public List<BookingDetails> getStudentBookings(int studentID) throws RemoteException {
        return studentService.viewStudentBooking(studentID);
    }
}
