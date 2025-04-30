package client.student.view;

import client.student.controller.CreateBookingController;
import client.student.controller.ReserveBookingPopUpController;
import client.student.controller.ViewSubjectController;
import client.student.model.CreateBookingModel;
import client.student.model.ReserveBookingPopUpModel;
import client.student.model.ViewSubjectModel;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.Booking;
import shared.classes.BookingDetails;
import shared.classes.SessionManager;
import shared.classes.TutorSession;
import shared.interfaces.StudentService;

import java.io.IOException;
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
    @FXML private TextField searchStudBookingTextField;
    @FXML private Label studBookingTitleLabel;
    @FXML private TableColumn<TutorSession, String> subjectColumn;
    @FXML private TableColumn<TutorSession, String> timeColumn;
    @FXML private TableColumn<TutorSession, String> typeColumn;
    @FXML private TableColumn<TutorSession, String> modeColumn;
    @FXML private TableColumn<TutorSession, String> priceColumn;
    @FXML private TableColumn<TutorSession, String> enrolledColumn;


    private final ObservableList<TutorSession> allBookings = FXCollections.observableArrayList();
    private CreateBookingController controller;

    @Override
    public void initialize(URL url, ResourceBundle loc) {
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        initializeTableColumns();
        initializeSearchListener();

        refreshButton.setOnAction(event -> {
            try {
                controller.refreshTable();
            } catch (RemoteException e) {
                showErrorAlert("Refresh Error", "Failed to refresh bookings: " + e.getMessage());
            }
        });
    }

    private void initializeSearchListener() {
        // store filtered results
        FilteredList<TutorSession> filteredData = new FilteredList<>(
                createReservationTableView.getItems(), p -> true
        );

        // replace curretn table content with search queries
        createReservationTableView.setItems(filteredData);

        // active listener naol u know im just rephrasing these commaents
        searchStudBookingTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(session -> {
                // if no search query show all
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // search input is converted to lowercase
                String lowerCaseFilter = newValue.toLowerCase();

                // check attributes if matches to the search query
                if (session.getSubjectName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches subject name
                }
                if (session.getAcademicLevel().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches academic level
                }
                if (String.valueOf(session.getSessionDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches date
                }
                if (String.valueOf(session.getSessionTime()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches time
                }
                if (session.getSessionMode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Matches mode (Online/Face-to-Face)
                }
                // Check session type (Solo/Group)
                String type = session.getMaximumStudents() == 1 ? "solo" : "group";
                if (type.contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // No matches found
            });
        });
    }

    private void initializeController() throws RemoteException {
        System.out.println("[CLIENT] Controller initialized!");
        try {
            StudentService service = new StudentServiceImpl(); // Initialize the service
            CreateBookingModel model = new CreateBookingModel(service);
            this.controller = new CreateBookingController(model, this);

            // Initial data load
            controller.refreshTable();
        } catch (RemoteException e) {
            showErrorAlert("Connection Error", "Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    private void initializeTableColumns() {
        // Existing columns setup
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDate())));

        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionTime())));

        durationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));

        academicLevelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAcademicLevel()));

        subjectColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSubjectName()));

        typeColumn.setCellValueFactory(cellData -> {
            int maxStudents = cellData.getValue().getMaximumStudents();
            String type = maxStudents == 1 ? "Solo" : "Group";
            return new SimpleStringProperty(type);
        });
        modeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionMode()));
        priceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionPrice())));
        enrolledColumn.setCellValueFactory(cellData -> {
            String numberOfStudents = (cellData.getValue().getNumberOfStudents() + " / " + cellData.getValue().getMaximumStudents());
                    return new SimpleStringProperty(numberOfStudents);
                });

        // Reserve button column setup
        reserveColumn.setCellFactory(param -> new TableCell<>() {
            private final Button reserveButton = new Button("Reserve");

            {
                reserveButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
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
        Platform.runLater(() -> {
            if (createReservationTableView != null) {
                allBookings.setAll(sessions);
                createReservationTableView.setItems(allBookings);
            }
        });
    }


    private void handleReserveAction(TutorSession session) throws Exception {
        // Verify active session
        String studentId = SessionManager.getCurrentUserId();
        if (studentId == null) {
            throw new Exception("Please login to make reservations");
        }
        showReservePopUp(session);

    }

    private void showReservePopUp(TutorSession session) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student/reserve_booking_window.fxml"));
            Parent root = loader.load();

            // Get the controller instance created by FXMLLoader
            ReserveBookingPopUpView popupView = loader.getController();

            // Initialize the popup's model and controller
            StudentService studentService = new StudentServiceImpl();
            ReserveBookingPopUpModel popupModel = new ReserveBookingPopUpModel(studentService);
            ReserveBookingPopUpController popupController = new ReserveBookingPopUpController(popupModel);

            // Set the controller in the view
            popupView.setController(popupController);
            popupView.setSelectedSession(session);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Reserve Booking");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh the table after popup closes
            if (controller != null) {
                controller.refreshTable();
            }
        } catch (RemoteException e) {
            showErrorAlert("Error", "Service initialization failed: " + e.getMessage());
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open reserve dialog: " + e.getMessage());
        }
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