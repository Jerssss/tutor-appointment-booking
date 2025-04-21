package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.Duration;
import shared.classes.Booking;
import client.student.controller.ModifyBookingController;
import shared.classes.BookingDetails;

public class UpdateBookingPopUp {
    @FXML private ComboBox<String> sessionDateComboBox;
    @FXML private ComboBox<String> sessionTimeComboBox;
    @FXML private ComboBox<String> sessionModeComboBox;
    @FXML private Button updateBookingButton;

    private Booking selectedBooking;
    private ModifyBookingController controller;

    public void setSelectedBooking(BookingDetails booking) {
        this.selectedBooking = booking;
        // Populate the combo boxes with the booking details
        sessionDateComboBox.setValue(booking.getSessionDate());
        sessionTimeComboBox.setValue(booking.getSessionTime());
        sessionModeComboBox.setValue(booking.getSessionMode());
    }

    public void setController(ModifyBookingController controller) {
        this.controller = controller;
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
        String newSessionDate = sessionDateComboBox.getValue();
        String newSessionTime = sessionTimeComboBox.getValue();
        String newSessionMode = sessionModeComboBox.getValue();

        // Call the method to modify the booking
        if (selectedBooking != null && controller != null) {
            controller.modifyBooking(
                    selectedBooking.getStudentID(),
                    selectedBooking.getSessionID(),
                    newSessionMode,
                    selectedBooking.getBookingStatus(),
                    selectedBooking.getSessionPrice()
            );
        }
    }
}