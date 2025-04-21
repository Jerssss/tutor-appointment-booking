package client.student.view;

import client.student.controller.ModifyBookingController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.Duration;
import shared.classes.BookingDetails;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class UpdateBookingPopUp {
    @FXML private DatePicker sessionDatePicker; // Changed from ComboBox to DatePicker
    @FXML private ComboBox<String> sessionTimeComboBox;
    @FXML private ComboBox<String> sessionModeComboBox;
    @FXML private Button updateBookingButton;

    private BookingDetails selectedBooking;
    private ModifyBookingController controller;

    public void setSelectedBooking(BookingDetails booking) {
        this.selectedBooking = booking;
        // Populate the fields with the booking details
        sessionDatePicker.setValue(LocalDate.parse(booking.getSessionDate()));
        sessionTimeComboBox.setValue(booking.getSessionTime());
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
                    selectedBooking.getStudentID(),
                    selectedBooking.getSessionID(),
                    newSessionMode,
                    newSessionDate,
                    newSessionTime,
                    selectedBooking.getBookingStatus(),
                    selectedBooking.getSessionPrice()
            );
        }
    }
}