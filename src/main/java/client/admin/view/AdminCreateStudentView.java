package client.admin.view;

import client.admin.controller.AdminCreateStudentController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminCreateStudentView {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private ComboBox academicLevelComboBox;
    @FXML
    private Button addStudentButton;
    private AdminCreateStudentController controller;
    private final int MAX_PHONENUM_LENGTH = 11;

    public void initialize() {
        initializeController();

        firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s-]*")) {
                firstNameTextField.setText(newValue.replaceAll("[^a-zA-Z\\s-]", ""));
            }
        });

        lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\s-]*")) {
                lastNameTextField.setText(newValue.replaceAll("[^a-zA-Z\\s-]", ""));
            }
        });

        phoneNumberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneNumberTextField.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > MAX_PHONENUM_LENGTH) {
                phoneNumberTextField.setText(oldValue); // Revert to previous if max length is exceeded
            }
        });

        firstNameTextField.setPromptText("e.g John");
        lastNameTextField.setPromptText("e.g Doe");
        phoneNumberTextField.setPromptText("e.g 01234567890");
        emailTextField.setPromptText("e.g johndoe@gmail.com");
        academicLevelComboBox.setPromptText("-- Select Academic Level --");
        academicLevelComboBox.getItems().addAll("High School", "College");
        addStudentButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing AdminCreateStudentController...");
        this.controller = new AdminCreateStudentController();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] AdminCreateStudentController successfully created.");
    }

    private void handleSave() {
        String fname = firstNameTextField.getText();
        String lname = lastNameTextField.getText();
        String phoneNumberStr = phoneNumberTextField.getText();
        String email = emailTextField.getText();
        String acadLvl = (String) academicLevelComboBox.getValue();

        StringBuilder errors = new StringBuilder();
        List<Control> invalidFields = new ArrayList<>();

        // Call validateFields method for checking empty fields
        validateFirstName(fname, errors, invalidFields);
        validateLastName(lname, errors, invalidFields);
        long pNum = validatePhoneNumber(phoneNumberStr, errors, invalidFields);
        validateEmail(email, errors, invalidFields);
        validateAcademicLevel(acadLvl, errors, invalidFields);

        if (pNum == -1 || !errors.isEmpty()) {
            showAlert("Validation Error", errors.toString(), invalidFields);
            return;  // Stop further processing if there are validation errors
        }


        // Pass data to the controller to save it in the database
        boolean success = controller.addNewStudent(fname, lname, pNum, email, acadLvl);

        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to add student                                 .",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Student added successfully                                 .",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        closeWindow();
    }

    private void validateFirstName(String fname, StringBuilder errors, List<Control> invalidFields) {
        if (fname == null || fname.trim().isEmpty()) {
            errors.append("• First name is required.\n");
            invalidFields.add(firstNameTextField);
        }
    }

    private void validateLastName(String lname, StringBuilder errors, List<Control> invalidFields) {
        if (lname == null || lname.trim().isEmpty()) {
            errors.append("• Last name is required.\n");
            invalidFields.add(lastNameTextField);
        }
    }

    private long validatePhoneNumber(String phoneString, StringBuilder errors, List<Control> invalidFields) {
        // Check if the phone number is empty
        if (phoneString == null || phoneString.trim().isEmpty()) {
            errors.append("• Phone number is required.\n");
            invalidFields.add(phoneNumberTextField);
            return -1; // Return -1 to indicate an invalid phone number
        }

        try {
            // Try to parse the phone number as a long
            long pNum = Long.parseLong(phoneString);

            // Check if the parsed phone number is a valid positive number
            if (pNum <= 0) {
                errors.append("• Phone number must be a valid positive number.\n");
                invalidFields.add(phoneNumberTextField);
                return -1; // Return -1 to indicate an invalid phone number
            }

            return pNum;  // Return the valid phone number
        } catch (NumberFormatException e) {
            // Handle case when the input cannot be parsed as a long
            errors.append("• Phone number must be a valid number.\n");
            invalidFields.add(phoneNumberTextField);
            return -1; // Return -1 to indicate an invalid phone number
        }
    }

    private void validateEmail(String email, StringBuilder errors, List<Control> invalidFields) {
        if (email == null || email.trim().isEmpty()) {
            errors.append("• Email is required.\n");
            invalidFields.add(emailTextField);
        } else {
            // You can add a more complex email validation logic here
            if (!email.contains("@") || !email.contains(".")) {
                errors.append("• Email is not valid.\n");
                invalidFields.add(emailTextField);
            }
        }
    }

    private void validateAcademicLevel(String acadLvl, StringBuilder errors, List<Control> invalidFields) {
        if (acadLvl == null || acadLvl.trim().isEmpty()) {
            errors.append("• Academic level is required.\n");
            invalidFields.add(academicLevelComboBox);
        }
    }

    private void showAlert(String title, String message, List<Control> invalidFields) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        for (Control field : invalidFields) {
            if (field instanceof TextField textField) {
                textField.setStyle("-fx-background-color: red;");
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.trim().isEmpty()) {
                        textField.setStyle("");
                    }
                });
            } else if (field instanceof ComboBox<?> comboBox) {
                comboBox.setStyle("-fx-background-color: #EBC7C7;");
                comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        comboBox.setStyle("");
                    }
                });
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addStudentButton.getScene().getWindow();
        stage.close();
    }

    public void addStudentButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addStudentButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addStudentButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addStudentButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}