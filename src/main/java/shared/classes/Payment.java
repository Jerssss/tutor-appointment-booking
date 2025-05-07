package shared.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Payment implements Serializable {
    private String paymentID;
    private String studentID;
    private String studentName;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private String paymentMethod;
    private double amount;

    // Constructor
    public Payment(String paymentID, String studentID, LocalDate date,  LocalTime time, String paymentMethod, double amount) {
        this.paymentID = paymentID;
        this.studentID = studentID;
        this.paymentDate = date;
        this.paymentTime = time;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public Payment(String paymentID, String studentID, String studName, LocalDate date,  LocalTime time, String paymentMethod, double amount) {
        this.paymentID = paymentID;
        this.studentID = studentID;
        this.studentName = studName;
        this.paymentDate = date;
        this.paymentTime = time;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public Payment(String paymentID, String studentID, String paymentMethod, double amount) {
        this.paymentID = paymentID;
        this.studentID = studentID;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    // Getters and Setters
    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public LocalTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Payment{" +
                "paymentID='" + paymentID + '\'' +
                ", studentID='" + studentID + '\'' +
                ", paymentDate=" + paymentDate +
                ", paymentTime=" + paymentTime +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                '}';
    }
}