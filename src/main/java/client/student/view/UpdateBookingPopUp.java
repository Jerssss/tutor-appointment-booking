package client.student.view;

import client.student.controller.ModifyBookingController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import shared.classes.BookingDetails;

import javax.swing.JOptionPane;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UpdateBookingPopUp {
    @FXML private DatePicker sessionDatePicker;
    @FXML private ComboBox<String> sessionTimeComboBox;
    @FXML private ComboBox<String> sessionModeComboBox;
    @FXML private Button updateBookingButton;

    private BookingDetails selectedBooking;
    private ModifyBookingController controller;

    public void setSelectedBooking(BookingDetails booking) {
        this.selectedBooking = booking;
        // Populate the fields with the booking details
        sessionDatePicker.setValue(LocalDate.parse(booking.getSessionDate()));
        populateSessionTimeComboBox(booking.getSessionTime());
        sessionModeComboBox.setValue(booking.getSessionMode());
    }

    public void setController(ModifyBookingController controller) {
        this.controller = controller;
    }

    @FXML
    private void initialize() {
        // Initialize the session mode ComboBox with options
        sessionModeComboBox.getItems().addAll("Online", "Face-to-Face");

        // Disable past dates and Sundays in the DatePicker
        sessionDatePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // Disable past dates and Sundays
                        if (date.isBefore(LocalDate.now()) || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Optional: Highlight disabled dates
                        }
                    }
                };
            }
        });
    }

    private void populateSessionTimeComboBox(String currentSessionTime) {
        // Parse the current session time with seconds
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime startTime = LocalTime.parse(currentSessionTime, timeFormatter);

        // Define the durations in minutes
        int[] durations = {60, 90, 120, 150};
        List<String> endTimes = new ArrayList<>();

        // Populate the ComboBox with the original session time first
        sessionTimeComboBox.getItems().clear();
        sessionTimeComboBox.getItems().add(currentSessionTime); // Add original session time

        // Calculate end times based on the durations
        for (int duration : durations) {
            LocalTime endTime = startTime.plusMinutes(duration);
            endTimes.add(endTime.format(timeFormatter)); // Keep the format with seconds
        }

        // Add calculated end times to the ComboBox
        sessionTimeComboBox.getItems().addAll(endTimes);

        // Set default value to the original session time
        sessionTimeComboBox.setValue(currentSessionTime);
    }

    @FXML
    private void updateBookingButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), updateBookingButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    @FXML
    private void updateBookingButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), updateBookingButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    @FXML
    private void handleUpdateBooking() {
        String newSessionDate = sessionDatePicker.getValue().toString(); // Get date from DatePicker
        String newSessionTime = sessionTimeComboBox.getValue();
        String newSessionMode = sessionModeComboBox.getValue();

        // Call the method to modify the booking
        if (selectedBooking != null && controller != null) {
            controller.modifyBooking(
                    String.valueOf(selectedBooking.getStudentID()),
                    selectedBooking.getSessionID(),
                    newSessionMode,
                    newSessionDate,
                    newSessionTime,
                    selectedBooking.getBookingStatus(),
                    selectedBooking.getSessionPrice()
            );
            // Show JOptionPane on success
            javax.swing.SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "Operation successful and edit pane has been closed",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            });
            // Close the dialog
            Stage stage = (Stage) updateBookingButton.getScene().getWindow();
            stage.close();
        }
    }
}