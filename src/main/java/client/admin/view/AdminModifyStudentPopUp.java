package client.admin.view;

import client.admin.controller.AdminModifyStudentPopUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.classes.Student;

import javax.swing.*;
import java.util.Date;

public class AdminModifyStudentPopUp {
    @FXML
    private Label studentIDLabel;
    @FXML
    private Label oldPassLabel;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button saveChangesButton;
    @FXML
    private ComboBox<String> studentVisibilityComboBox;

    private Student student;
    private AdminModifyStudentPopUpController controller;

    public void initialize() {
        initializeController();
        studentVisibilityComboBox.getItems().addAll("Available", "Archived");
        studentVisibilityComboBox.setValue("Available");
        saveChangesButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing AdminModifyStudentPopUpController...");
        this.controller = new AdminModifyStudentPopUpController();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] AdminModifyStudentPopUpController successfully created.");
    }

    public void setStudent(Student student) {
        this.student = student;

        if (studentIDLabel != null && oldPassLabel != null) {
            studentIDLabel.setText(student.getUserID());
            oldPassLabel.setText(student.getPassword());
        }
    }

    private void handleSave() {
        String newPass = "";
        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            newPass = oldPassLabel.getText();
        } else {
            newPass = passwordTextField.getText();
        }

        String id = student.getUserID();
        String visibility = getSelectedAvailability();
        boolean success = controller.modifyStudent(id, newPass, visibility);

        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to update student.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Student updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveChangesButton.getScene().getWindow();
        stage.close();
    }

    public String getSelectedAvailability(){
        return studentVisibilityComboBox.getValue();
    }
}