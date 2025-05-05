package shared.classes;

import java.io.Serializable;

public class Student extends User implements Serializable {
    private double balance;
    private String academicLevel;

    // used for storing the students from the database
    public Student(String userID, String firstName, String lastName, long phoneNumber, String email, String role, String password, double balance, String academicLevel, String visibility) {
        super(userID, firstName, lastName, phoneNumber, email, role, password, visibility);
        this.balance = balance;
        this.academicLevel = academicLevel;
    }

    public Student(String userID, String firstName, String lastName, long phoneNumber, String email, String role, double balance, String academicLevel) {
        super(userID, firstName, lastName, phoneNumber, email, role);
        this.balance = balance;
        this.academicLevel = academicLevel;
    }

    public Student(String firstName, String lastName, long phoneNumber, String email, String role, double balance, String academicLevel) {
        super(firstName, lastName, phoneNumber, email, role);
        this.balance = balance;
        this.academicLevel = academicLevel;
    }

    public Student(String userID, String firstName, String lastName){
        super(userID, firstName, lastName);
    }

    // Getter and Setter for balance
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Getter and Setter for academicLevel
    public String getAcademicLevel() {
        return academicLevel;
    }

    public void setAcademicLevel(String academicLevel) {
        this.academicLevel = academicLevel;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Student{" +
                "userID=" + getUserID() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + getRole() + '\'' +
                ", balance=" + balance + '\'' +
                ", academicLevel='" + academicLevel + '\'' +
                ", visibility='" + getVisibility() + '\'' +
                '}';
    }
}