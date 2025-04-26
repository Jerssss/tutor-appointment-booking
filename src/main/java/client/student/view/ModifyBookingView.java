package client.student.view;

import client.student.controller.ModifyBookingController;
import client.student.model.ModifyBookingModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.BookingDetails;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModifyBookingView implements Initializable {
    @FXML private TextField searchStudResTextField;
    @FXML private Button refreshButton;
    @FXML private TableView<BookingDetails> modResTableView;
    @FXML private TableColumn<BookingDetails, String> roomNumberColumn;
    @FXML private TableColumn<BookingDetails, String> terminalIDColumn;
    @FXML private TableColumn<BookingDetails, String> reservationDateColumn;
    @FXML private TableColumn<BookingDetails, String> startTimeColumn;
    @FXML private TableColumn<BookingDetails, String> endTimeColumn;
    @FXML private TableColumn<BookingDetails, String> editColumn;
    @FXML private TableColumn<BookingDetails, String> cancelColumn;

    private final ObservableList<BookingDetails> allBookings = FXCollections.observableArrayList();
    private ModifyBookingController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        initializeRowFactory();
        System.out.println("[CLIENT] Table columns initialized successfully.");
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[CLIENT] Controller initialized successfully.");
        initializeSearchListener();
        System.out.println("[CLIENT] Search field initialized successfully.");
    }

    private void initializeTableColumns() {
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        terminalIDColumn.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionMode"));
        editColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
        cancelColumn.setCellValueFactory(param -> new SimpleStringProperty(""));

        // Set cell factories for edit and cancel columns
        editColumn.setCellFactory(createEditButtonCellFactory());
        cancelColumn.setCellFactory(createCancelButtonCellFactory());
    }

    private void initializeRowFactory() {
        modResTableView.setRowFactory(tv -> new TableRow<BookingDetails>() {
            @Override
            protected void updateItem(BookingDetails booking, boolean empty) {
                super.updateItem(booking, empty);
                getStyleClass().remove("cancelled-row");
                getStyleClass().remove("past-date-row");

                if (!empty && booking != null) {
                    if ("Cancelled".equals(booking.getBookingStatus())) {
                        getStyleClass().add("cancelled-row");
                    } else if (isPastSession(booking.getSessionDate(), booking.getSessionTime())) {
                        getStyleClass().add("past-date-row");
                    }
                }
            }
        });
    }

    private void initializeController() throws RemoteException {
        String studentID = SessionManager.getCurrentUserId();
        if (studentID == null || studentID.isEmpty()) {
            return;
        }
        StudentService service = new StudentServiceImpl();
        ModifyBookingModel model = new ModifyBookingModel(service);
        this.controller = new ModifyBookingController(model, this);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
    }

    public void initializeSearchListener() {
        searchStudResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBookings(newValue.toLowerCase().trim());
        });
    }

    public void searchBookings(String query) {
        if (allBookings.isEmpty()) {
            return;
        }

        if (query == null || query.isEmpty()) {
            modResTableView.setItems(allBookings);
            return;
        }

        List<BookingDetails> filteredList = allBookings.stream()
                .filter(booking -> booking.getSubjectName().toLowerCase().contains(query) ||
                        booking.getTutorName().toLowerCase().contains(query) ||
                        booking.getSessionDate().toLowerCase().contains(query) ||
                        booking.getSessionTime().toLowerCase().contains(query) ||
                        booking.getSessionMode().toLowerCase().contains(query))
                .collect(Collectors.toList());
        modResTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    public void updateTable(List<BookingDetails> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("[CLIENT] No bookings to display.");
        } else {
            System.out.println("[CLIENT] Updating table with " + bookings.size() + " bookings.");
            allBookings.setAll(bookings); // Populate the ObservableList
            modResTableView.setItems(allBookings);
            modResTableView.refresh();
        }
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

    private boolean isPastSession(String sessionDate, String sessionTime) {
        LocalDateTime sessionDateTime = LocalDateTime.parse(sessionDate + "T" + sessionTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return sessionDateTime.isBefore(LocalDateTime.now());
    }

    private Callback<TableColumn<BookingDetails, String>, TableCell<BookingDetails, String>> createEditButtonCellFactory() {
        return column -> new TableCell<BookingDetails, String>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                editButton.setOnAction(event -> {
                    BookingDetails booking = getTableRow().getItem();
                    if (booking != null && "Approved".equals(booking.getBookingStatus()) && !isPastSession(booking.getSessionDate(), booking.getSessionTime())) {
                        showEditDialog(booking);
                    } else {
                        showErrorAlert("Edit Not Allowed", "Only approved bookings that have not yet occurred can be edited.");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                BookingDetails booking = getTableRow().getItem();
                if (empty || booking == null || booking.getSubjectName() == null || booking.getSubjectName().isEmpty()) {
                    setGraphic(null);
                } else {
                    editButton.setDisable(!"Approved".equals(booking.getBookingStatus()) || isPastSession(booking.getSessionDate(), booking.getSessionTime()));
                    setGraphic(editButton);
                }
            }
        };
    }

    private Callback<TableColumn<BookingDetails, String>, TableCell<BookingDetails, String>> createCancelButtonCellFactory() {
        return column -> new TableCell<BookingDetails, String>() {
            private final Button cancelButton = new Button("Cancel");

            {
                cancelButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                cancelButton.setOnAction(event -> {
                    BookingDetails booking = getTableRow().getItem();
                    if (booking != null && "Approved".equals(booking.getBookingStatus()) && !isPastSession(booking.getSessionDate(), booking.getSessionTime())) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirm Cancellation");
                        alert.setHeaderText("Cancel Booking");
                        alert.setContentText("Are you sure you want to cancel this booking?");

                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK ) {
                                controller.cancelBooking(booking.getSessionID());
                            }
                        });
                    } else {
                        showErrorAlert("Cancellation Not Allowed", "Only approved bookings that have not yet occurred can be cancelled.");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                BookingDetails booking = getTableRow().getItem();
                if (empty || booking == null || booking.getSubjectName() == null || booking.getSubjectName().isEmpty()) {
                    setGraphic(null);
                } else {
                    cancelButton.setDisable(!"Approved".equals(booking.getBookingStatus()) || isPastSession(booking.getSessionDate(), booking.getSessionTime()));
                    setGraphic(cancelButton);
                }
            }
        };
    }

    private void showEditDialog(BookingDetails booking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student/modify_booking_window.fxml"));
            Parent root = loader.load();
            UpdateBookingPopUp controller = loader.getController();
            controller.setSelectedBooking(booking);
            controller.setController(this.controller);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Booking");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showErrorAlert("Error", "Could not open edit dialog.");
        }
    }

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setController(ModifyBookingController controller) {
        this.controller = controller;
    }
}