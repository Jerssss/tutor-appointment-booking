package shared.classes;

import java.time.LocalDate;
import java.time.LocalTime;

public class BalanceDetails extends Student {
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private int sessionDuration;
    private String courseName;
    private String sessionMode;
    private String tutorName;
    private String bookingStatus;

    public BalanceDetails(String userID, String firstName, String lastName, long phoneNumber, String email, String role, double balance, String academicLevel, LocalDate sessionDate, LocalTime sessionTime, int sessionDuration, String courseName, String sessionMode, String tutorName, String bookingStatus) {
        super(userID, firstName, lastName, phoneNumber, email, role, balance, academicLevel);
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.sessionDuration = sessionDuration;
        this.courseName = courseName;
        this.sessionMode = sessionMode;
        this.tutorName = tutorName;
        this.bookingStatus = bookingStatus;
    }

    // Getters and Setters
    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(LocalTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}