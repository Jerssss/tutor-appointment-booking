package shared.interfaces;

import shared.classes.*;

import java.rmi.RemoteException;

public interface StudentService {
    Booking createBooking(int studentID, int sessionID, String sessionMode, String bookingStatus, double sessionPrice) throws RemoteException;
    Booking viewStudentBooking(int studentID) throws RemoteException;
    Booking modifyBooking(int studentID, int sessionID, String newSessionMode,
                          String newBookingStatus, double newSessionPrice) throws RemoteException;
    Subject viewSubject();
    LessonPlan viewLessonPlan();
    Payment viewPaymentHistory();
    Student viewStudentBalance();
    Payment createPayment();
}
