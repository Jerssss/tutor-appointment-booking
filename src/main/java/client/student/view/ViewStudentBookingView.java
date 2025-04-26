package client.student.view;

import client.student.controller.CreateBookingController;
import client.student.controller.ViewStudentBookingController;
import client.student.model.CreateBookingModel;
import client.student.model.ViewStudentBookingModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.BookingDetails;
import shared.interfaces.StudentService;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class ViewStudentBookingView implements Initializable {

    @FXML private VBox centerPane;
    @FXML private TableColumn<BookingDetails, String> courseColumn;
    @FXML private TableColumn<BookingDetails, String> dateColumn;
    @FXML private TableColumn<BookingDetails, String> durationColumn;
    @FXML private TableColumn<BookingDetails, String> modeColumn;
    @FXML private Button refreshButton;
    @FXML private TextField searchResTextField;
    @FXML private TableColumn<BookingDetails, String> statusColumn;
    @FXML private TableColumn<BookingDetails, String> timeColumn;
    @FXML private TableColumn<BookingDetails, String> tutorColumn;
    @FXML private Label viewResLabel;
    @FXML private TableView<BookingDetails> viewResTableView;

    private ViewStudentBookingController controller;

    @Override
    public void initialize(URL url, ResourceBundle loc) {
        initializeTableColumns();
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        initializeSearchListener();
    }

    private void initializeSearchListener() {
        // filtered results
        FilteredList<BookingDetails> filteredData = new FilteredList<>(
                viewResTableView.getItems(), p -> true
        );

        // make the results that are fitered show in the table instaed of the original
        viewResTableView.setItems(filteredData);

        // the listener to the searhc field
        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(booking -> {
                // If search text is empty, show all bookings
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // convert search input to lower case
                String lowerCaseFilter = newValue.toLowerCase();

                // check relevant attrbibutes
                if (booking.getSubjectName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches course name
                }
                if (booking.getTutorName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches tutor name
                }
                if (booking.getSessionDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches date
                }
                if (booking.getBookingStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches status
                }
                if (booking.getSessionMode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches mode (Online/Face-to-Face)
                }
                return false; // No matches found
            });
        });
    }

    private void initializeController() throws RemoteException {
        System.out.println("[CLIENT] Controller initialized!");
        StudentService service = new StudentServiceImpl(); // Initialize the service
        ViewStudentBookingModel model = new ViewStudentBookingModel(service);
        this.controller = new ViewStudentBookingController(model, this);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
    }
    public void initializeTableColumns() {
        // Set up cell value factories for each column
        courseColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubjectName()));

        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionDate()));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionTime()));

        durationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));

        tutorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTutorName()));

        modeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionMode()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBookingStatus()));
    }

    public void updateTable(ObservableList<BookingDetails> bookings) {
        viewResTableView.setItems(bookings);

        if (bookings.isEmpty()) {
            viewResTableView.setPlaceholder(
                    new Label("No bookings found for your account"));
        }
    }
    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
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
}
