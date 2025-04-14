package shared.classes;

import java.io.Serializable;

public class User implements Serializable {
    private int userID;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private String password;

    // Primary constructor
    public User(int userID, String firstName, String lastName, String phoneNumber,
                String email, String role, String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // Secondary constructor (for login results)
    public User(String userID, String firstName, String lastName,
                long phoneNumber, String email, String role) {
        this(Integer.parseInt(userID),
                firstName,
                lastName,
                String.valueOf(phoneNumber),
                email,
                role,
                null); // password not needed for logged-in user
    }




    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    // Override toString for easy object representation
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
