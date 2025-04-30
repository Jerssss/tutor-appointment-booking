package client.student.view;

import client.student.controller.ReserveBookingPopUpController;
import client.student.model.ReserveBookingPopUpModel;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import shared.classes.Booking;
import shared.classes.SessionManager;
import shared.classes.Tutor;
import shared.classes.TutorSession;

public class ReserveBookingPopUpView {

    // UI Components
    @FXML private Label acadLevelLabel, dateLabel, durationLabel, modeLabel,
            priceLabel, subjectLabel, timeLabel, typeLabel, tutorNameLabel, tutorIDLabel;
    @FXML private Button confirmButton, cancelButton;

    // Payment Options
    @FXML private RadioButton payNowRadio, payLaterRadio, bdoRadio, gcashRadio, unionBankRadio;
    @FXML private ToggleGroup paymentGroup, bankGroup;
    @FXML private VBox paymentOptionsVBox;

    @FXML private TextField amountToPayTextField;

    private TutorSession selectedSession;
    private ReserveBookingPopUpController controller;

    @FXML
    public void initialize() {
        setupPaymentToggleListener();
        gcashRadio.setSelected(true);
        paymentOptionsVBox.setVisible(false);

        // Add listener to validate payment amount
        amountToPayTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                amountToPayTextField.setText(oldValue);
            }
        });
    }

    private void setupPaymentToggleListener() {
        payNowRadio.selectedProperty().addListener((obs, oldVal, payNowSelected) -> {
            paymentOptionsVBox.setVisible(payNowSelected);
            confirmButton.setText(payNowSelected ? "Confirm and Pay" : "Reserve Later");
        });
    }

    @FXML
    private void handleConfirmBooking() {
        try {
            String studentId = SessionManager.getCurrentUserId();
            if (studentId == null) {
                throw new Exception("Please login to make reservations");
            }

            boolean payNow = payNowRadio.isSelected();
            String paymentMethod = payNow ? getSelectedBank() : null;
            double amountPaid = 0;

            if (payNow) {
                try {
                    amountPaid = Double.parseDouble(amountToPayTextField.getText());
                    if (amountPaid <= 0 || amountPaid > selectedSession.getSessionPrice()) {
                        throw new Exception("Amount must be between 0 and " + selectedSession.getSessionPrice());
                    }
                } catch (NumberFormatException e) {
                    throw new Exception("Please enter a valid payment amount");
                }
            }

            controller.processBooking(
                    studentId,
                    selectedSession,
                    payNow,
                    paymentMethod,
                    amountPaid
            );

            showAlert("Success", payNow ?
                    String.format("Payment successful! %.2f via %s. Remaining balance: %.2f",
                            amountPaid,
                            paymentMethod,
                            selectedSession.getSessionPrice() - amountPaid) :
                    "Booking reserved. Pay later in your Balance view."
            );

            closeWindow();
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private String getSelectedBank() {
        if (bdoRadio.isSelected()) return "BDO";
        if (unionBankRadio.isSelected()) return "Union Bank";
        return "GCash";
    }

    public void setSelectedSession(TutorSession session) {
        this.selectedSession = session;
        populateSessionDetails();
    }

    private void populateSessionDetails() {
        try {
            dateLabel.setText(String.valueOf(selectedSession.getSessionDate()));
            timeLabel.setText(String.valueOf(selectedSession.getSessionTime()));
            durationLabel.setText(selectedSession.getSessionDuration() + " mins");

            Tutor tutor = controller.getTutorDetails(selectedSession.getTutorID());
            if (tutor != null) {
                tutorNameLabel.setText(tutor.getFirstName() + " " + tutor.getLastName());
            } else {
                tutorNameLabel.setText("Unknown Tutor");
            }

            tutorIDLabel.setText(selectedSession.getTutorID());
            acadLevelLabel.setText(selectedSession.getAcademicLevel());
            subjectLabel.setText(selectedSession.getSubjectName());
            modeLabel.setText(selectedSession.getSessionMode());
            priceLabel.setText(String.format("₱%,d", selectedSession.getSessionPrice()));
            typeLabel.setText(selectedSession.getMaximumStudents() == 1 ? "Solo" : "Group");
        } catch (Exception e) {
            showAlert("Error", "Failed to load session details: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        cancelButton.getScene().getWindow().hide();
    }

    // Button animations
    public void confirmButtonExited() { animateButton(confirmButton, 1.0); }
    public void confirmButtonHovered() { animateButton(confirmButton, 1.1); }
    public void cancelButtonExited() { animateButton(cancelButton, 1.0); }
    public void cancelButtonHovered() { animateButton(cancelButton, 1.1); }

    private void animateButton(Button button, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    public void setController(ReserveBookingPopUpController controller) {
        this.controller = controller;
    }
}