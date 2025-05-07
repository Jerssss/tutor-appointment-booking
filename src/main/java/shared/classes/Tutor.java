package shared.classes;

import java.io.Serializable;
import java.util.List;

public class Tutor extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> expertises;
    private String expertise;

    public Tutor(String userID, String firstName, String lastName, long phoneNumber, String email, String role, String password, String expertise, String visibility) {
        super(userID, firstName, lastName, phoneNumber, email, role, password, visibility);
        this.expertise = expertise;
    }

    public Tutor(String userID, String firstName, String lastName, long phoneNumber, String email, String role, String password, String expertise) {
        super(userID, firstName, lastName, phoneNumber, email, role, password);
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