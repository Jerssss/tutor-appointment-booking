package shared.interfaces;

import shared.classes.*;

import java.rmi.RemoteException;
import java.util.List;

public interface StudentService {
    Booking createBooking(String studentID, String sessionID, String sessionMode, String bookingStatus, double sessionPrice) throws RemoteException;
    List<BookingDetails> viewStudentBooking(int studentID) throws RemoteException;
    List<TutorSession> viewAvailableSessions() throws RemoteException;
    Booking modifyBooking(String studentID, String sessionID, String newSessionMode, String newBookingStatus, double newSessionPrice, String newSessionDate, String newSessionTime) throws RemoteException;
    boolean cancelBooking(String sessionID) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    List<LessonPlan> viewHighSchoolLessonPlan() throws RemoteException;
    List<LessonPlan> viewCollegeLessonPlan() throws RemoteException;
    List<PaymentDetails> viewPaymentHistory(String studentID) throws RemoteException;
    List<BalanceDetails> viewStudentBalanceDetails(String studentID) throws RemoteException;
    Payment createPayment(String studentId, double amount, String paymentMethod) throws RemoteException;
    boolean updateStudentBalance(String studentId, double amount) throws RemoteException;
    double getStudentBalance(String studentID) throws RemoteException;
}
