package client.admin.view;

import client.admin.controller.AdminCreatePaymentController;
import client.admin.controller.AdminCreateStudentController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminCreatePaymentView {
    @FXML
    private ComboBox<String> studentIdComboBox;
    @FXML
    private ComboBox<String> modeOfPaymentComboBox;
    @FXML
    private TextField amountTextField;
    @FXML
    private Button payButton;
    @FXML
    private Button cancelButton;

    private AdminCreatePaymentController controller;

    public void initialize() {
        initializeController();

        // Populate payment methods
        studentIdComboBox.getItems().addAll(controller.getStudentList());
        modeOfPaymentComboBox.getItems().addAll("Gcash", "BDO", "Union Bank");
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing AdminCreateStudentController...");
        this.controller = new AdminCreatePaymentController();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] AdminCreateStudentController successfully created.");
    }

    @FXML
    private void handlePay() {
        String studentID = studentIdComboBox.getValue();
        String paymentMethod = modeOfPaymentComboBox.getValue();
        String amountStr = amountTextField.getText();

        StringBuilder errors = new StringBuilder();
        List<Control> invalidFields = new ArrayList<>();

        // Call validateFields method for checking empty fields
        validateStudentId(studentID, errors, invalidFields);
        validatePaymentMethod(paymentMethod, errors, invalidFields);
        int amount = validateAmount(amountStr, errors, invalidFields);

        if (amount == -1 || !errors.isEmpty()) {
            showAlert("Validation Error", errors.toString(), invalidFields);
            return;  // Stop further processing if there are validation errors
        }


        // Pass data to the controller to save it in the database
        boolean success = controller.addNewPayment(studentID, paymentMethod, amount);

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

    private void validateStudentId(String studID, StringBuilder errors, List<Control> invalidFields) {
        if (studID == null || studID.trim().isEmpty()) {
            errors.append("• Student is required.\n");
            invalidFields.add(studentIdComboBox);
        }
    }

    private void validatePaymentMethod(String pMethod, StringBuilder errors, List<Control> invalidFields) {
        if (pMethod == null || pMethod.trim().isEmpty()) {
            errors.append("• Mode of Payment is required.\n");
            invalidFields.add(modeOfPaymentComboBox);
        }
    }

    private int validateAmount(String amt, StringBuilder errors, List<Control> invalidFields) {
        // Check if the phone number is empty
        if (amt == null || amt.trim().isEmpty()) {
            errors.append("• Amount is required.\n");
            invalidFields.add(amountTextField);
            return -1; // Return -1 to indicate an invalid phone number
        }

        try {
            // Try to parse the phone number as a long
            int amount = Integer.parseInt(amt);

            // Check if the parsed phone number is a valid positive number
            if (amount <= 0) {
                errors.append("• Amount must be a valid positive number.\n");
                invalidFields.add(amountTextField);
                return -1; // Return -1 to indicate an invalid phone number
            }

            return amount;  // Return the valid phone number
        } catch (NumberFormatException e) {
            // Handle case when the input cannot be parsed as a long
            errors.append("• Amount must be a valid number.\n");
            invalidFields.add(amountTextField);
            return -1; // Return -1 to indicate an invalid phone number
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

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) payButton.getScene().getWindow()).close();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Hover effects
    public void payButtonHovered() {
        animateButton(payButton, 0.9);
    }

    public void payButtonExited() {
        animateButton(payButton, 1.0);
    }

    public void cancelButtonHovered() {
        animateButton(cancelButton, 0.9);
    }

    public void cancelButtonExited() {
        animateButton(cancelButton, 1.0);
    }

    private void animateButton(Button button, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
}