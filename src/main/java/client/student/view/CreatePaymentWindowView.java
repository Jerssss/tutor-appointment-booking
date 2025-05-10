package client.student.view;

import client.StudentTutorClient;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Locale;

public class CreatePaymentWindowView {
    @FXML private ComboBox<String> modeOfPaymentComboBox;
    @FXML private TextField amountTextField;
    @FXML private Button payButton;
    @FXML private Button cancelButton;

    private String studentId;
    private StudentService service;

    public void initializeData(String studentId, double currentBalance) throws RemoteException {
        this.studentId = studentId;
        this.service = StudentTutorClient.getStudentService();

        // Populate payment methods
        modeOfPaymentComboBox.getItems().addAll(
                "Gcash", "BDO", "Union Bank"
        );
    }

    @FXML
    private void handlePay() {
        String paymentMethod = modeOfPaymentComboBox.getValue();
        String amountText = amountTextField.getText();

        // Validate inputs
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            showErrorAlert("Error", "Please select a payment method");
            return;
        }

        if (amountText.isEmpty()) {
            showErrorAlert("Error", "Please enter an amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showErrorAlert("Error", "Amount must be positive");
                return;
            }

            // Process payment
            service.createPayment(studentId, amount, paymentMethod);
            boolean success = service.updateStudentBalance(studentId, -amount);

            if (success) {
                showSuccessAlert("Success", String.format("Payment of ₱%,.2f processed!", amount));
                closeWindow();
            } else {
                showErrorAlert("Error", "Failed to update balance");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Error", "Invalid amount format");
        } catch (RemoteException e) {
            showErrorAlert("Error", "Payment failed: " + e.getMessage());
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