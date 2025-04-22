package shared.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class PaymentDetails extends Payment implements Serializable {
    private String invoiceID;
    private String courseSubject;
    private String sessionMode;
    private String status;

    // Constructor
    public PaymentDetails(String paymentID, String studentID, LocalDate paymentDate, LocalTime paymentTime, String paymentMethod, double amount, String invoiceID, String courseSubject, String sessionMode, String status) {
        super(paymentID, studentID, paymentDate, paymentTime, paymentMethod, amount);
        this.invoiceID = invoiceID;
        this.courseSubject = courseSubject;
        this.sessionMode = sessionMode;
        this.status = status;
    }

    // Getters and Setters
    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getCourseSubject() {
        return courseSubject;
    }

    public void setCourseSubject(String courseSubject) {
        this.courseSubject = courseSubject;
    }

    public String getSessionMode() {
        return sessionMode;
    }

    public void setSessionMode(String sessionMode) {
        this.sessionMode = sessionMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}