package client.student.view;

import client.student.controller.ReserveSessionPopUpController;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import shared.classes.SessionManager;
import shared.classes.Student;
import shared.classes.Tutor;
import shared.classes.TutorSession;

public class ReserveSessionPopUpView {

    @FXML private Label acadLevelLabel, dateLabel, durationLabel, modeLabel,
            priceLabel, subjectLabel, timeLabel, typeLabel, tutorNameLabel, tutorIDLabel, sessionIDLabel;
    @FXML private Button confirmButton, cancelButton;
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

                // Name validation check
                try {
                    Student student = controller.getStudentDetails(studentId);
                    Tutor tutor = controller.getTutorDetails(selectedSession.getTutorID());

                    if (student == null || tutor == null) {
                        showAlert(Alert.AlertType.ERROR, "Validation Error", "Could not verify user details.");
                        return;
                    }

                    String studentName = formatName(student.getFirstName(), student.getLastName());
                    String tutorName = formatName(tutor.getFirstName(), tutor.getLastName());

                    if (studentName.equalsIgnoreCase(tutorName)) {
                        showAlert(Alert.AlertType.ERROR, "Booking Conflict", "You cannot book your own session.");
                        return;
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Error verifying details: " + e.getMessage());
                    return;
                }

                boolean payNow = payNowRadio.isSelected();
                String paymentMethod = payNow ? getSelectedBank() : null;
                double amountPaid = 0;

                if (payNow) {
                    try {
                        amountPaid = Double.parseDouble(amountToPayTextField.getText());
                        if (amountPaid <= 0 || amountPaid > selectedSession.getSessionPrice()) {
                            showAlert(Alert.AlertType.ERROR, "Payment Error",
                                    String.format("Amount must be between 0 and ₱%,.2f", selectedSession.getSessionPrice()));
                            return;
                        }
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Payment Error", "Invalid payment amount format.");
                        return;
                    }
                }

                controller.processBooking(studentId, selectedSession, payNow, paymentMethod, amountPaid);

                showAlert(Alert.AlertType.INFORMATION, "Success", payNow ?
                        String.format("Paid ₱%,.2f via %s\nRemaining: ₱%,.2f",
                                amountPaid, paymentMethod, selectedSession.getSessionPrice() - amountPaid) :
                        "Booking reserved. Pay later in Balance view.");

                closeWindow();
            } catch (Exception e) {
                handleBookingError(e);
            }
        });
    }

    private String formatName(String firstName, String lastName) {
        return (firstName + " " + lastName).trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private void handleBookingError(Exception e) {
        String msg = e.getMessage().toLowerCase();
        if (msg.contains("overlap")) {
            showAlert(Alert.AlertType.ERROR, "Schedule Conflict", "You have an overlapping session.");
        } else if (msg.contains("full")) {
            showAlert(Alert.AlertType.ERROR, "Session Full", "This session is no longer available.");
        } else if (msg.contains("database")) {
            showAlert(Alert.AlertType.ERROR, "Server Error", "Could not connect to database.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Booking Failed", e.getMessage());
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
        Platform.runLater(() -> {
            sessionIDLabel.setText(selectedSession.getSessionID());
            dateLabel.setText(selectedSession.getSessionDate().toString());
            timeLabel.setText(selectedSession.getSessionTime().toString());
            durationLabel.setText(selectedSession.getSessionDuration() + " mins");
            tutorIDLabel.setText(selectedSession.getTutorID());
            acadLevelLabel.setText(selectedSession.getAcademicLevel());
            subjectLabel.setText(selectedSession.getSubjectName());
            modeLabel.setText(selectedSession.getSessionMode());
            priceLabel.setText(String.format("₱%,.2f", (double) selectedSession.getSessionPrice()));
            typeLabel.setText(selectedSession.getMaximumStudents() == 1 ? "Solo" : "Group");

            try {
                Tutor tutor = controller.getTutorDetails(selectedSession.getTutorID());
                tutorNameLabel.setText(tutor != null ?
                        tutor.getFirstName() + " " + tutor.getLastName() : "Tutor Not Found");
            } catch (Exception e) {
                tutorNameLabel.setText("Tutor Info Unavailable");
                System.out.println("[ERROR] Tutor details: " + e.getMessage());
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
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