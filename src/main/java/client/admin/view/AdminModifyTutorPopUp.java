package client.admin.view;

import client.admin.controller.AdminModifyTutorPopUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.classes.Tutor;

import javax.swing.*;

public class AdminModifyTutorPopUp {
    @FXML
    private Label tutorIDLabel;
    @FXML
    private Label oldPassLabel;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button saveChangesButton;
    private Tutor tutor;
    private AdminModifyTutorPopUpController controller;

    public void initialize() {
        initializeController();
        saveChangesButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing AdminModifyTutorPopUpController...");
        this.controller = new AdminModifyTutorPopUpController();
        System.out.println("[CLIENT] AdminModifyTutorPopUpController successfully created.");
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;

        if (tutorIDLabel != null && oldPassLabel != null) {
            tutorIDLabel.setText(tutor.getUserID());
            oldPassLabel.setText(tutor.getPassword());
        }
    }

    private void handleSave() {
        String newPass = passwordTextField.getText();

        if (tutor == null || newPass == null || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Please check the form.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = tutor.getUserID();
        boolean success = controller.modifyTutor(id, newPass);

        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to update tutor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Tutor updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) saveChangesButton.getScene().getWindow();
        stage.close();
    }
}