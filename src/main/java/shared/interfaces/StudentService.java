package shared.interfaces;

import shared.classes.*;

import java.rmi.RemoteException;
import java.util.List;

public interface StudentService {
    Booking createBooking(int studentID, String sessionID, String sessionMode, String bookingStatus, double sessionPrice) throws RemoteException;
    List<BookingDetails> viewStudentBooking(int studentID) throws RemoteException;
    List<TutorSession> viewAvailableSessions() throws RemoteException;
    Booking modifyBooking(int studentID, String sessionID, String newSessionMode, String newBookingStatus, double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException;
    boolean cancelBooking(String sessionID) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    List<LessonPlan> viewHighSchoolLessonPlan() throws RemoteException;
    List<LessonPlan> viewCollegeLessonPlan() throws RemoteException;
    List<PaymentDetails> viewPaymentHistory(int studentID) throws RemoteException;
    Student viewStudentBalance();
    Payment createPayment(String studentID, double amount, String paymentMethod) throws RemoteException;
}
