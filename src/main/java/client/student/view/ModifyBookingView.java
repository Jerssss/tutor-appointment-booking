package client.student.view;

import client.student.controller.ModifyBookingController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import shared.classes.BookingDetails;

import java.util.List;

public class ModifyBookingView {
    @FXML
    private Button refreshButton;

    @FXML
    private TableView<BookingDetails> modResTableView;

    @FXML
    private TableColumn<BookingDetails, String> roomNumberColumn;

    @FXML
    private TableColumn<BookingDetails, String> terminalIDColumn;

    @FXML
    private TableColumn<BookingDetails, String> reservationDateColumn;

    @FXML
    private TableColumn<BookingDetails, String> startTimeColumn;

    @FXML
    private TableColumn<BookingDetails, String> endTimeColumn;

    @FXML
    private TableColumn<BookingDetails, String> editColumn;

    @FXML
    private TableColumn<BookingDetails, String> cancelColumn;

    private ModifyBookingController controller;

    public void initialize() {
        // Initialize table columns
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        terminalIDColumn.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionMode"));
        editColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
        cancelColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
    }

    public void setController(ModifyBookingController controller) {
        this.controller = controller;
    }

    @FXML
    private void handleRefresh() {
        System.out.println("[CLIENT] Refresh button clicked.");
        if (controller != null) {
            controller.refreshTable();
        }
    }

    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount (1);
        st.setAutoReverse(false);
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void updateTable(List<BookingDetails> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("[CLIENT] No bookings to display.");
        } else {
            System.out.println("[CLIENT] Updating table with " + bookings.size() + " bookings.");
            modResTableView.getItems().setAll(bookings);
            modResTableView.refresh();
        }
    }

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}