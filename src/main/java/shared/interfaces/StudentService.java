package shared.interfaces;

import shared.classes.*;

import java.rmi.RemoteException;
import java.util.List;

public interface StudentService {
    Booking createBooking(int studentID, int sessionID, String sessionMode, String bookingStatus, double sessionPrice) throws RemoteException;
    Booking viewStudentBooking(int studentID) throws RemoteException;
    Booking modifyBooking(int studentID, int sessionID, String newSessionMode, String newBookingStatus, double newSessionPrice) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    LessonPlan viewLessonPlan();
    Payment viewPaymentHistory();
    Student viewStudentBalance();
    Payment createPayment(String studentID, double amount, String paymentMethod) throws RemoteException;
}
