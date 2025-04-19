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
    public void initialize() {
        initializeController();
        academicLevelComboBox.getItems().addAll("High School", "College");
        addStudentButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing AdminCreateStudentController...");
        this.controller = new AdminCreateStudentController();
        System.out.println("[CLIENT] AdminCreateStudentController successfully created.");
    }

    private void handleSave() {
        String fname = firstNameTextField.getText();
        String lname = lastNameTextField.getText();
        long pNum = Long.parseLong(phoneNumberTextField.getText());
        String email = emailTextField.getText();
        String acadLvl = (String) academicLevelComboBox.getValue();

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
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        closeWindow();
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
