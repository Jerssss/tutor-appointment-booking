package client.student.controller;

import client.student.model.CreatePaymentWindowModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.services.StudentServiceImpl;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Locale;

public class CreatePaymentWindowController {
    @FXML private Label priceLabel;
    @FXML private ToggleGroup bankGroup;
    @FXML private Button payButton;
    @FXML private Button cancelButton;

    private String studentId;
    private double paymentAmount;
    private StudentService service;

    public void initializeData(String studentId, double currentBalance) {
        this.studentId = studentId;
        this.paymentAmount = Math.abs(currentBalance);
        this.service = new StudentServiceImpl();

        // Format and display the payment amount
        NumberFormat pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        priceLabel.setText(pesoFormat.format(paymentAmount));
    }

    @FXML
    private void handlePay() {
        RadioButton selectedMethod = (RadioButton) bankGroup.getSelectedToggle();
        if (selectedMethod == null) {
            showAlert("Payment Error", "Please select a payment method.");
            return;
        }

        String paymentMethod = selectedMethod.getText();
        try {
            // Process payment and update balance
            service.createPayment(studentId, paymentAmount, paymentMethod);
            boolean success = service.updateStudentBalance(studentId, paymentAmount);

            if (success) {
                showAlert("Success", "Payment processed successfully!");
                closeWindow();
            } else {
                showAlert("Error", "Failed to update balance.");
            }
        } catch (RemoteException e) {
            showAlert("Error", "Payment failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) payButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}