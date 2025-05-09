package client.admin.view;

import client.admin.controller.AdminModifyTutorPopUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.classes.Tutor;

import javax.swing.*;
import java.util.Date;

public class AdminModifyTutorPopUp {
    @FXML
    private Label tutorIDLabel;
    @FXML
    private Label oldPassLabel;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button saveChangesButton;

    @FXML
    private ComboBox<String> tutorVisibilityComboBox;


    private Tutor tutor;
    private AdminModifyTutorPopUpController controller;

    public void initialize() {
        initializeController();
        tutorVisibilityComboBox.getItems().addAll("Available", "Archived");
        tutorVisibilityComboBox.setValue("Available");
        saveChangesButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing AdminModifyTutorPopUpController...");
        this.controller = new AdminModifyTutorPopUpController();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] AdminModifyTutorPopUpController successfully created.");
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;

        if (tutorIDLabel != null && oldPassLabel != null) {
            tutorIDLabel.setText(tutor.getUserID());
            oldPassLabel.setText(tutor.getPassword());
        }
    }

    private void handleSave() {
        String newPass = "";
        if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()) {
            newPass = oldPassLabel.getText();
        } else {
            newPass = passwordTextField.getText();
        }


        if (tutor == null) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Please check the form.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }




        System.out.println("newPass: " + newPass);
        String id = tutor.getUserID();
        String visibility = getSelectedAvailability();
        boolean success = controller.modifyTutor(id, newPass, visibility);

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

    public String getSelectedAvailability(){
        return tutorVisibilityComboBox.getValue();
    }
}