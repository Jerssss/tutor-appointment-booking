package client.student.view;

import client.student.controller.ViewPaymentHistoryController;
import client.student.model.ViewPaymentHistoryModel;
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
import shared.classes.PaymentDetails;
import shared.classes.SessionManager;
import shared.interfaces.StudentService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class ViewPaymentHistoryView implements Initializable {
    @FXML private TextField searchResTextField;
    @FXML private Button refreshButton;
    @FXML private TableView<PaymentDetails> viewResTableView;
    @FXML private TableColumn<PaymentDetails, String> invoiceColumn;
    @FXML private TableColumn<PaymentDetails, String> courseSubjectColumn;
    @FXML private TableColumn<PaymentDetails, String> sessionModeColumn;
    @FXML private TableColumn<PaymentDetails, String> paymentDateColumn;
    @FXML private TableColumn<PaymentDetails, String> paymentTimeColumn;
    @FXML private TableColumn<PaymentDetails, String> paymentMethodColumn;
    @FXML private TableColumn<PaymentDetails, Double> amountColumn;
    @FXML private TableColumn<PaymentDetails, String> statusColumn;

    private final ObservableList<PaymentDetails> allPayments = FXCollections.observableArrayList();
    private ViewPaymentHistoryController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        initializeSearchListener();
        System.out.println("[CLIENT] Payment history view initialized successfully.");
        try {
            initializeController(); // Initialize the controller here
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Automatically refresh the table when the view is loaded
        if (controller != null) {
            controller.refreshTable();
        }
    }

    private void initializeTableColumns() {
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("invoiceID"));
        courseSubjectColumn.setCellValueFactory(new PropertyValueFactory<>("courseSubject"));
        sessionModeColumn.setCellValueFactory(new PropertyValueFactory<>("sessionMode"));
        paymentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentDate().toString()));
        paymentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentTime().toString()));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void initializeSearchListener() {
        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchPayments(newValue.toLowerCase().trim());
        });
    }

    public void searchPayments(String query) {
        if (allPayments.isEmpty()) {
            return;
        }

        if (query == null || query.isEmpty()) {
            viewResTableView.setItems(allPayments);
            return;
        }

        List<PaymentDetails> filteredList = allPayments.stream()
                .filter(payment -> payment.getInvoiceID().toLowerCase().contains(query) ||
                        payment.getCourseSubject().toLowerCase().contains(query) ||
                        payment.getSessionMode().toLowerCase().contains(query) ||
                        payment.getPaymentMethod().toLowerCase().contains(query) ||
                        payment.getStatus().toLowerCase().contains(query))
                .toList();
        viewResTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    public void updatePaymentHistory(List<PaymentDetails> paymentHistory) {
        if (paymentHistory == null || paymentHistory.isEmpty()) {
            System.out.println("[CLIENT] No payment history to display.");
        } else {
            System.out.println("[CLIENT] Updating table with " + paymentHistory.size() + " payments.");
            allPayments.setAll(paymentHistory); // Populate the ObservableList
            viewResTableView.setItems(allPayments);
            viewResTableView.refresh();
        }
    }

    public void updateTable(ObservableList<PaymentDetails> observablePayments) {
        if (observablePayments == null || observablePayments.isEmpty()) {
            System.out.println("[CLIENT] No payments to display.");
        } else {
            System.out.println("[CLIENT] Updating table with " + observablePayments.size() + " payments.");
            allPayments.setAll(observablePayments); // Populate the ObservableList
            viewResTableView.setItems(allPayments);
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

    public void setController(ViewPaymentHistoryController controller) {
        this.controller = controller;
    }

    private void initializeController() throws RemoteException {
        String studentID = SessionManager.getCurrentUserId();
        if (studentID == null || studentID.isEmpty()) {
            return;
        }
        StudentService service = new StudentServiceImpl();
        ViewPaymentHistoryModel model = new ViewPaymentHistoryModel(service);
        this.controller = new ViewPaymentHistoryController(model, this);

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
}
