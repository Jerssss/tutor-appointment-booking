package client.admin.view;

import client.admin.controller.AdminCreateStudentController;
import client.admin.controller.AdminCreateTutorController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class AdminCreateTutorView {
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField expertiseTextField;
    @FXML
    private Button addTutorButton;
    private AdminCreateTutorController controller;
    public void initialize() {
        initializeController();
        addTutorButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing AdminCreateTutorController...");
        this.controller = new AdminCreateTutorController();
        System.out.println("[CLIENT] AdminCreateTutorController successfully created.");
    }

    private void handleSave() {
        String fname = firstNameTextField.getText();
        String lname = lastNameTextField.getText();
        long pNum = Long.parseLong(phoneNumberTextField.getText());
        String email = emailTextField.getText();
        String expertise = expertiseTextField.getText();

        // Pass data to the controller to save it in the database
        boolean success = controller.addNewTutor(fname, lname, pNum, email, expertise);

        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to add tutor                                 .",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Tutor added successfully                                 .",
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) addTutorButton.getScene().getWindow();
        stage.close();
    }

    public void addTutorButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addTutorButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addTutorButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addTutorButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}
