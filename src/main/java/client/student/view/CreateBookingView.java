package client.student.view;

import client.student.controller.CreateBookingController;
import client.student.controller.ViewSubjectController;
import client.student.model.CreateBookingModel;
import client.student.model.ViewSubjectModel;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.Booking;
import shared.classes.SessionManager;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class CreateBookingView implements Initializable {
    @FXML private TableColumn<TutorSession, String> academicLevelColumn;
    @FXML private VBox centerPane;
    @FXML private TableView<TutorSession> createReservationTableView;
    @FXML private TableColumn<TutorSession, String> dateColumn;
    @FXML private TableColumn<TutorSession, String> durationColumn;
    @FXML private Button refreshButton;
    @FXML private TableColumn<TutorSession, Void> reserveColumn;
    @FXML private TextField searchStudResTextField;
    @FXML private Label studResTitleLabel;
    @FXML private TableColumn<TutorSession, String> subjectColumn;
    @FXML private TableColumn<TutorSession, String> timeColumn;
    @FXML private TableColumn<TutorSession, String> typeColumn;
    @FXML private TableColumn<TutorSession, String> modeColumn;

    private final ObservableList<TutorSession> allBookings = FXCollections.observableArrayList();
    private CreateBookingController controller;

    @Override
    public void initialize(URL url, ResourceBundle loc) {
        initializeTableColumns();
        initializeController();
        initializeSearchListener();
    }

    private void initializeSearchListener() {

    }

    private void initializeController() {
        System.out.println("[CLIENT] Controller initialized!");
        StudentService service = new StudentServiceImpl(); // Initialize the service
        CreateBookingModel model = new CreateBookingModel(service);
        this.controller = new CreateBookingController(model, this);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
    }

    private void initializeTableColumns() {
        // Existing columns setup
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionDate()));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionTime()));

        durationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));

        academicLevelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubjectLevel()));

        subjectColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubjectName()));

        typeColumn.setCellValueFactory(cellData -> {
            int maxStudents = cellData.getValue().getMaximumStudents();
            String type = maxStudents == 1 ? "Solo" : "Group";
            return new SimpleStringProperty(type);
        });
        modeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionMode()));

        // Reserve button column setup
        reserveColumn.setCellFactory(param -> new TableCell<>() {
            private final Button reserveButton = new Button("Reserve");

            {
                reserveButton.getStyleClass().add("reserve-button");
                reserveButton.setOnAction(event -> {
                    TutorSession session = getTableView().getItems().get(getIndex());
                    try {
                        handleReserveAction(session);
                    } catch (RemoteException e) {
                        showErrorAlert("Booking Error", "Failed to create booking: " + e.getMessage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : reserveButton);
            }
        });
    }


    public void updateTable(List<TutorSession> sessions) {
        allBookings.setAll(sessions);
        createReservationTableView.setItems(allBookings);
    }

    private void handleReserveAction(TutorSession session) throws Exception {
        // Verify active session
        String studentId = SessionManager.getCurrentUserId();
        if (studentId == null) {
            throw new Exception("Please login to make reservations");
        }


        // Create booking through controller
        Booking newBooking = controller.createBooking(
                studentId,
                session.getSessionID(),
                session.getSessionMode(),
                "Pending", // Initial status
                session.getSessionPrice()
        );

        // Update UI
        showSuccessAlert("Booking Created",
                "Successfully reserved " + session.getSessionMode() + " session");

        // Refresh available sessions
        controller.refreshTable();
    }

    private void showSuccessAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Animation methods
    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

}