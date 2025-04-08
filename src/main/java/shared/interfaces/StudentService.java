package shared.interfaces;

import shared.classes.*;

public interface StudentService {
    Booking createBooking();
    Booking viewStudentBooking();
    Booking modifyBooking();
    Subject viewSubject();
    LessonPlan viewLessonPlan();
    Payment viewPaymentHistory();
    Student viewStudentBalance();
}
