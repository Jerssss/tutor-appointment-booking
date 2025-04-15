package shared.classes;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    private String paymentID;
    private String studentID;
    private double amount;
    private LocalDateTime paymentDateTime;
    private String paymentMethod;

    // Constructor
    public Payment(String paymentID, String studentID, double amount, LocalDateTime paymentDateTime, String paymentMethod) {
        this.paymentID = paymentID;
        this.studentID = studentID;
        this.amount = amount;
        this.paymentDateTime = paymentDateTime;
        this.paymentMethod = paymentMethod;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Payment{" +
                "paymentID=" + paymentID +
                ", studentID=" + studentID +
                ", amount=" + amount +
                ", paymentDateTime=" + paymentDateTime +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}