package client.student.view;

import client.student.controller.ReserveSessionPopUpController;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import shared.classes.SessionManager;
import shared.classes.Tutor;
import shared.classes.TutorSession;

public class ReserveSessionPopUpView {

    // UI Components
    @FXML private Label acadLevelLabel, dateLabel, durationLabel, modeLabel,
            priceLabel, subjectLabel, timeLabel, typeLabel, tutorNameLabel, tutorIDLabel, sessionIDLabel;
    @FXML private Button confirmButton, cancelButton;

    // Payment Options
    @FXML private RadioButton payNowRadio, payLaterRadio, bdoRadio, gcashRadio, unionBankRadio;
    @FXML private ToggleGroup paymentGroup, bankGroup;
    @FXML private VBox paymentOptionsVBox;

    @FXML private TextField amountToPayTextField;

    private TutorSession selectedSession;
    private ReserveSessionPopUpController controller;

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
        Platform.runLater(() -> {
            try {
                String studentId = SessionManager.getCurrentUserId();
                if (studentId == null) {
                    showAlert(Alert.AlertType.ERROR, "Session Error", "Please log in to make reservations.");
                    return;
                }

                boolean payNow = payNowRadio.isSelected();
                String paymentMethod = payNow ? getSelectedBank() : null;
                double amountPaid = 0;

                if (payNow) {
                    try {
                        amountPaid = Double.parseDouble(amountToPayTextField.getText());
                        if (amountPaid <= 0 || amountPaid > selectedSession.getSessionPrice()) {
                            showAlert(Alert.AlertType.ERROR, "Payment Error", String.format("Amount must be between 0 and ₱%,.2f.", (double) selectedSession.getSessionPrice()));
                            return;
                        }
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Payment Error", "Please enter a valid payment amount.");
                        return;
                    }
                }

                controller.processBooking(
                        studentId,
                        selectedSession,
                        payNow,
                        paymentMethod,
                        amountPaid
                );

                showAlert(Alert.AlertType.INFORMATION, "Success", payNow ?
                        String.format("Payment successful! ₱%,.2f via %s. Remaining balance: ₱%,.2f",
                                amountPaid,
                                paymentMethod,
                                (double) selectedSession.getSessionPrice() - amountPaid) :
                        "Booking reserved successfully. Pay later in your Balance view."
                );

                closeWindow();
            } catch (Exception e) {
                String message = e.getMessage().toLowerCase();
                if (message.contains("you have an active session that overlaps")) {
                    showAlert(Alert.AlertType.ERROR, "Booking Conflict", "Cannot reserve session: You have an active session that overlaps with this time slot.");
                } else if (message.contains("session may be full")) {
                    showAlert(Alert.AlertType.ERROR, "Booking Error", "Cannot reserve session: The session is full or unavailable.");
                } else if (message.contains("database error")) {
                    showAlert(Alert.AlertType.ERROR, "Server Error", "Unable to reserve session due to a server error.");
                } else if (message.contains("payment amount cannot exceed session price")) {
                    showAlert(Alert.AlertType.ERROR, "Payment Error", "Payment amount cannot exceed the session price.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Booking Error", "Unable to process booking: " + e.getMessage());
                }
            }
        });
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
        Platform.runLater(() -> {
            // Set session details
            sessionIDLabel.setText(selectedSession.getSessionID());
            dateLabel.setText(String.valueOf(selectedSession.getSessionDate()));
            timeLabel.setText(String.valueOf(selectedSession.getSessionTime()));
            durationLabel.setText(selectedSession.getSessionDuration() + " mins");
            tutorIDLabel.setText(selectedSession.getTutorID());
            acadLevelLabel.setText(selectedSession.getAcademicLevel());
            subjectLabel.setText(selectedSession.getSubjectName());
            modeLabel.setText(selectedSession.getSessionMode());
            priceLabel.setText(String.format("₱%,.2f", (double) selectedSession.getSessionPrice()));
            typeLabel.setText(selectedSession.getMaximumStudents() == 1 ? "Solo" : "Group");

            // Handle tutor details separately
            try {
                Tutor tutor = controller.getTutorDetails(selectedSession.getTutorID());
                tutorNameLabel.setText(tutor != null ? tutor.getFirstName() + " " + tutor.getLastName() : "Unknown Tutor");
            } catch (Exception e) {
                tutorNameLabel.setText("Unknown Tutor");
                // Log the error without showing a popup
                System.out.println("[CLIENT | " + new java.util.Date() + "] Failed to load tutor details: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
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

    public void setController(ReserveSessionPopUpController controller) {
        this.controller = controller;
    }
}