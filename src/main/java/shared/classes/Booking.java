package shared.classes;

import java.io.Serializable;

public class Booking implements Serializable {
    private String studentID;
    private String sessionID;
    private String sessionMode;
    private String bookingStatus;
    private double sessionPrice;

    public Booking(){
        this.studentID = null;
        this.sessionID = null;
        this.sessionMode = null;
        this.bookingStatus = null;
        this.sessionPrice = 0.0;
    }
    // Constructor
    public Booking(String studentID, String sessionID, String sessionMode, String bookingStatus, double sessionPrice) {
        this.studentID = studentID;
        this.sessionID = sessionID;
        this.sessionMode = sessionMode;
        this.bookingStatus = bookingStatus;
        this.sessionPrice = sessionPrice;
    }

    // Getters and Setters
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public double getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(double sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Booking{" +
                "studentID=" + studentID +
                ", sessionID=" + sessionID +
                ", sessionMode='" + sessionMode + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", sessionPrice=" + sessionPrice +
                '}';
    }
}

