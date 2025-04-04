package shared.classes;

public class Tutor extends User {
    private String expertise;

    //pwede ba to?
    // Constructor
    public Tutor(int userID, String firstName, String lastName, String phoneNumber, String email, String role, String expertise) {
        super(userID, firstName, lastName, phoneNumber, email, role);
        this.expertise = expertise;
    }

    // Getter and Setter for expertise
    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "Tutor{" +
                "userID=" + getUserID() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + getRole() + '\'' +
                ", expertise='" + expertise + '\'' +
                '}';
    }
}
