package client.student.model;

import shared.classes.Booking;
import shared.classes.BookingDetails;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ModifyBookingModel {
    private final StudentService service;

    public ModifyBookingModel(StudentService service) {
        this.service = service;
    }

    public List<BookingDetails> fetchBookings(int studentID) throws RemoteException {
        System.out.println("[CLIENT] Fetching bookings for studentID: " + studentID);
        List<BookingDetails> bookings = service.viewStudentBooking(studentID);
        System.out.println("[CLIENT] Fetched " + bookings.size() + " bookings.");
        return bookings;
    }

    public Booking modifyBooking(String studentID, String sessionID, String newSessionMode, String newBookingStatus, double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException {
        // Modify booking using the service
        return service.modifyBooking(studentID, sessionID, newSessionMode, newBookingStatus, newSessionPrice, newSessionDate, newSessionTime);
    }

    public boolean cancelBooking(String sessionID) throws RemoteException {
        // Call the service to cancel the booking
        return service.cancelBooking(sessionID);
    }
}