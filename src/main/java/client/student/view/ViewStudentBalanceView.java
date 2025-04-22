package client.student.view;

import client.student.controller.ViewStudentBalanceController;
import client.student.model.ViewStudentBalanceModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.BalanceDetails;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewStudentBalanceView implements Initializable {
    @FXML private TextField searchBalTextField;
    @FXML private Button refreshButton;
    @FXML private Button createPaymentButton;
    @FXML private TableView<BalanceDetails> viewResTableView;
    @FXML private TableColumn<BalanceDetails, String> dateColumn;
    @FXML private TableColumn<BalanceDetails, String> timeColumn;
    @FXML private TableColumn<BalanceDetails, String> durationColumn;
    @FXML private TableColumn<BalanceDetails, String> courseColumn;
    @FXML private TableColumn<BalanceDetails, String> modeColumn;
    @FXML private TableColumn<BalanceDetails, String> tutorColumn;
    @FXML private TableColumn<BalanceDetails, String> statusColumn;

    private final ObservableList<BalanceDetails> allBalanceDetails = FXCollections.observableArrayList();
    private ViewStudentBalanceController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        initializeSearchListener();
        System.out.println("[CLIENT] Balance details view initialized successfully.");
        initializeController(); // Initialize the controller here

        // Automatically refresh the table when the view is loaded
        if (controller != null) {
            controller.refreshTable();
        }
    }

    private void initializeTableColumns() {
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionDate().toString()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionTime().toString()));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionMode"));
        tutorColumn.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
    }

    private void initializeSearchListener() {
        searchBalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchBalanceDetails(newValue.toLowerCase().trim());
        });
    }

    public void searchBalanceDetails(String query) {
        if (allBalanceDetails.isEmpty()) {
            return;
        }

        if (query == null || query.isEmpty()) {
            viewResTableView.setItems(allBalanceDetails);
            return;
        }

        List<BalanceDetails> filteredList = allBalanceDetails.stream()
                .filter(balance -> balance.getCourseName().toLowerCase().contains(query) ||
                        balance.getSessionMode().toLowerCase().contains(query) ||
                        balance.getTutorName().toLowerCase().contains(query) ||
                        balance.getBookingStatus().toLowerCase().contains(query))
                .toList();
        viewResTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    public void updateTable(ObservableList<BalanceDetails> balanceDetails) {
        if (balanceDetails == null || balanceDetails.isEmpty()) {
            System.out.println("[CLIENT] No balance details to display.");
        } else {
            System.out.println("[CLIENT] Updating table with " + balanceDetails.size() + " balance details.");
            allBalanceDetails.setAll(balanceDetails); // Populate the ObservableList
            viewResTableView.setItems(allBalanceDetails);
            viewResTableView.refresh();
        }
    }

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setController(ViewStudentBalanceController controller) {
        this.controller = controller;
    }

    private void initializeController() {
        String studentID = SessionManager.getCurrentUserId();
        if (studentID == null || studentID.isEmpty()) {
            return;
        }
        StudentService service = new StudentServiceImpl();
        ViewStudentBalanceModel model = new ViewStudentBalanceModel(service);
        this.controller = new ViewStudentBalanceController(model, this);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
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

    public void createPaymentButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), createPaymentButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void createPaymentButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), createPaymentButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

}