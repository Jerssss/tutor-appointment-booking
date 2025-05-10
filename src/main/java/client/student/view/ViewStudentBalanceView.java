package client.student.view;

import client.student.controller.ViewStudentBalanceController;
import client.student.model.ViewStudentBalanceModel;
import javafx.animation.ScaleTransition;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.BalanceDetails;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ViewStudentBalanceView implements Initializable {
    @FXML private TextField searchBalTextField;
    @FXML private Button refreshButton;
    @FXML private Button createPaymentButton;
    @FXML private TableView<BalanceDetails> viewBalanceTableView;
    @FXML private TableColumn<BalanceDetails, String> dateColumn;
    @FXML private TableColumn<BalanceDetails, String> timeColumn;
    @FXML private TableColumn<BalanceDetails, String> durationColumn;
    @FXML private TableColumn<BalanceDetails, String> courseColumn;
    @FXML private TableColumn<BalanceDetails, String> modeColumn;
    @FXML private TableColumn<BalanceDetails, String> tutorColumn;
    @FXML private TableColumn<BalanceDetails, String> statusColumn;
    @FXML private Label remBalanceLabel;

    private final ObservableList<BalanceDetails> allBalanceDetails = FXCollections.observableArrayList();
    private ViewStudentBalanceController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        initializeSearchListener();
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (controller != null) controller.refreshTable();
    }

    // Initialize table columns
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
        // Create a FilteredList wrapping the allBalanceDetails ObservableList
        FilteredList<BalanceDetails> filteredData = new FilteredList<>(allBalanceDetails, p -> true);

        // Set the FilteredList as the items for the TableView
        viewBalanceTableView.setItems(filteredData);

        // Add a listener to the search TextField
        searchBalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(balance -> {
                // If the search field is empty or null, show all balance details
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                // Convert search input to lowercase for case-insensitive search
                String lowerCaseFilter = newValue.toLowerCase().trim();

                // Check if any balance attributes match the search query
                try {
                    if (balance.getCourseName() != null &&
                            balance.getCourseName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches course name
                    }
                    if (balance.getSessionMode() != null &&
                            balance.getSessionMode().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches session mode
                    }
                    if (balance.getTutorName() != null &&
                            balance.getTutorName().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches tutor name
                    }
                    if (balance.getBookingStatus() != null &&
                            balance.getBookingStatus().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches booking status
                    }
                    if (balance.getSessionDate() != null &&
                            balance.getSessionDate().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches date
                    }
                    if (balance.getSessionTime() != null &&
                            balance.getSessionTime().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true; // Matches time
                    }
                } catch (Exception e) {
                    // Log any errors and skip this balance detail
                    System.err.println("Error processing balance detail: " + e.getMessage());
                    return false;
                }

                return false; // No matches found
            });
        });
    }

    @FXML
    private void handleCreatePayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/student/create_payment_window.fxml"));
            Parent root = loader.load();

            CreatePaymentWindowView paymentView = loader.getController();
            String studentId = SessionManager.getCurrentUserId();
            double currentBalance = controller.getCurrentBalance();

            paymentView.initializeData(studentId, currentBalance);

            Stage paymentStage = new Stage();
            paymentStage.initModality(Modality.APPLICATION_MODAL);
            paymentStage.initOwner(createPaymentButton.getScene().getWindow());
            paymentStage.setTitle("Create Payment");
            paymentStage.setScene(new Scene(root));
            paymentStage.showAndWait();

            controller.refreshTable();
        } catch (IOException e) {
            showErrorAlert("Error", "Payment window error: " + e.getMessage());
        }
    }

    public void updateTable(ObservableList<BalanceDetails> balanceDetails) {
        if (balanceDetails == null || balanceDetails.isEmpty()) {
            System.out.println("[CLIENT | " + new Date() + "] No balance details to display.");
            allBalanceDetails.clear();
        } else {
            System.out.println("[CLIENT | " + new Date() + "] Updating table with " + balanceDetails.size() + " balance details.");
            allBalanceDetails.setAll(balanceDetails); // Populate the ObservableList
        }
        viewBalanceTableView.refresh();
    }

    public void updateBalanceDisplay(double balance) {
        // Create Philippine Peso formatter
        NumberFormat pesoFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));

        String formattedBalance = pesoFormat.format(Math.abs(balance));

        if (balance < 0) {
            remBalanceLabel.setStyle("-fx-text-fill: red;");
            formattedBalance = "-" + formattedBalance;
        } else {
            remBalanceLabel.setStyle("-fx-text-fill: green;");
        }

        remBalanceLabel.setText(formattedBalance);
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

    private void initializeController() throws RemoteException {
        String studentID = SessionManager.getCurrentUserId();
        if (studentID == null || studentID.isEmpty()) {
            System.out.println("[CLIENT | " + new Date() + "] No student ID found, skipping controller initialization.");
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
        System.out.println("[CLIENT | " + new Date() + "] Refresh button clicked.");
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