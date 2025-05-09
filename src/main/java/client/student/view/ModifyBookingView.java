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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import shared.classes.BookingDetails;
import shared.classes.SessionManager;
import server.services.StudentServiceImpl;
import shared.interfaces.StudentService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModifyBookingView implements Initializable {
    @FXML private TextField searchStudBookingTextField;
    @FXML private Button refreshButton;
    @FXML private TableView<BookingDetails> modBookingTableView;
    @FXML private TableColumn<BookingDetails, String> courseColumn;
    @FXML private TableColumn<BookingDetails, String> tutorColumn;
    @FXML private TableColumn<BookingDetails, String> sessionColumn;
    @FXML private TableColumn<BookingDetails, String> sessionTimeColumn;
    @FXML private TableColumn<BookingDetails, String> sessionModeColumn;
    @FXML private TableColumn<BookingDetails, String> editColumn;
    @FXML private TableColumn<BookingDetails, String> cancelColumn;

    private final ObservableList<BookingDetails> allBookings = FXCollections.observableArrayList();
    private ModifyBookingController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        initializeRowFactory();
        System.out.println("[CLIENT | "+ new Date()+ "] Table columns initialized successfully.");
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[CLIENT | "+ new Date()+ "] Controller initialized successfully.");
        initializeSearchListener();
        System.out.println("[CLIENT | "+ new Date()+ "] Search field initialized successfully.");
    }

    private void initializeTableColumns() {
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        tutorColumn.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        sessionColumn.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));
        sessionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionTime"));
        sessionModeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionMode"));

        // empty placeholder properties
        editColumn.setCellValueFactory(param -> new SimpleStringProperty(""));
        cancelColumn.setCellValueFactory(param -> new SimpleStringProperty(""));

        // set icon-button factories
        editColumn.setCellFactory(createEditButtonCellFactory());
        cancelColumn.setCellFactory(createCancelButtonCellFactory());
    }

    private void initializeRowFactory() {
        modBookingTableView.setRowFactory(tv -> new TableRow<BookingDetails>() {
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
        controller.refreshTable();
    }

    private void initializeSearchListener() {
        searchStudBookingTextField.textProperty().addListener((obs, oldVal, newVal) ->
                searchBookings(newVal.toLowerCase().trim())
        );
    }

    public void searchBookings(String query) {
        if (allBookings.isEmpty()) return;

        if (query == null || query.isEmpty()) {
            modBookingTableView.setItems(allBookings);
            return;
        }
        List<BookingDetails> filtered = allBookings.stream()
                .filter(b -> b.getSubjectName().toLowerCase().contains(query)
                        || b.getTutorName().toLowerCase().contains(query)
                        || b.getSessionDate().toLowerCase().contains(query)
                        || b.getSessionTime().toLowerCase().contains(query)
                        || b.getSessionMode().toLowerCase().contains(query))
                .collect(Collectors.toList());

        modBookingTableView.setItems(FXCollections.observableArrayList(filtered));
    }

    public void updateTable(List<BookingDetails> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("[CLIENT | "+ new Date()+ "] No bookings to display.");
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] Updating table with " + bookings.size() + " bookings.");
            allBookings.setAll(bookings);
            modBookingTableView.setItems(allBookings);
            modBookingTableView.refresh();
        }
    }

    @FXML
    private void handleRefresh() {
        System.out.println("[CLIENT | "+ new Date()+ "] Refresh button clicked.");
        if (controller != null) controller.refreshTable();
    }

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

    private boolean isPastSession(String sessionDate, String sessionTime) {
        LocalDateTime dt = LocalDateTime.parse(sessionDate + "T" + sessionTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dt.isBefore(LocalDateTime.now());
    }

    private Callback<TableColumn<BookingDetails, String>, TableCell<BookingDetails, String>> createEditButtonCellFactory() {
        return col -> new TableCell<>() {
            private final Button editButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                editButton.setGraphic(iv);
                editButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                editButton.setOnAction(e -> {
                    BookingDetails b = getTableRow().getItem();
                    if (b != null && "Approved".equals(b.getBookingStatus()) && !isPastSession(b.getSessionDate(), b.getSessionTime())) {
                        showEditDialog(b);
                    } else {
                        showErrorAlert("Edit Not Allowed","Only future, approved bookings may be edited.");
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                BookingDetails b = getTableRow().getItem();
                if (empty || b == null) {
                    setGraphic(null);
                } else {
                    editButton.setDisable(!"Approved".equals(b.getBookingStatus()) || isPastSession(b.getSessionDate(), b.getSessionTime()));
                    setGraphic(editButton);
                }
            }
        };
    }

    private Callback<TableColumn<BookingDetails, String>, TableCell<BookingDetails, String>> createCancelButtonCellFactory() {
        return col -> new TableCell<>() {
            private final Button cancelButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/DeleteIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                cancelButton.setGraphic(iv);
                cancelButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                cancelButton.setOnAction(e -> {
                    BookingDetails b = getTableRow().getItem();
                    if (b != null && "Approved".equals(b.getBookingStatus()) && !isPastSession(b.getSessionDate(), b.getSessionTime())) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirm Cancellation");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Are you sure you want to cancel this booking?");
                        confirm.showAndWait().ifPresent(r -> {
                            if (r == ButtonType.OK) {
                                controller.cancelBooking(b.getSessionID());
                            }
                        });
                    } else {
                        showErrorAlert("Cancellation Not Allowed","Only future, approved bookings may be cancelled.");
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                BookingDetails b = getTableRow().getItem();
                if (empty || b == null) {
                    setGraphic(null);
                } else {
                    cancelButton.setDisable(!"Approved".equals(b.getBookingStatus()) || isPastSession(b.getSessionDate(), b.getSessionTime()));
                    setGraphic(cancelButton);
                }
            }
        };
    }

    private void showEditDialog(BookingDetails booking) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student/modify_booking_window.fxml"));
            Parent root = loader.load();
            UpdateBookingPopUp pop = loader.getController();
            pop.setSelectedBooking(booking);
            pop.setController(this.controller);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Booking");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showErrorAlert("Error","Could not open edit dialog.");
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
