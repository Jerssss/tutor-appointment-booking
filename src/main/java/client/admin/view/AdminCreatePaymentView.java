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
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Date;
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
            boolean success = true;

            if (success) {
                showSuccessAlert("Success", String.format("Payment of ₱%,.2f processed!", amount));
                closeWindow();
            } else {
                showErrorAlert("Error", "Failed to update balance");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Error", "Invalid amount format");
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