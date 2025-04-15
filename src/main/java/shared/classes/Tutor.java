package shared.classes;

import java.io.Serializable;

public class Tutor extends User implements Serializable {
    private String expertise;

    public Tutor(String userID, String firstName, String lastName, long phoneNumber, String email, String role, String expertise) {
        super(userID, firstName, lastName, phoneNumber, email, role);
        this.expertise = expertise;
    }

    public Tutor(String firstName, String lastName, long phoneNumber, String email, String role, String expertise) {
        super(firstName, lastName, phoneNumber, email, role);
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
