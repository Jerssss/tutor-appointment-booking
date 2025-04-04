package shared.classes;

public class Student extends User {
    private double balance;
    private String academicLevel;

    //pwede ba to?
    // Constructor
    public Student(int userID, String firstName, String lastName, String phoneNumber, String email, String role, double balance, String academicLevel) {
        super(userID, firstName, lastName, phoneNumber, email, role);
        this.balance = balance;
        this.academicLevel = academicLevel;
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
                ", balance=" + balance +
                ", academicLevel='" + academicLevel + '\'' +
                '}';
    }
}
