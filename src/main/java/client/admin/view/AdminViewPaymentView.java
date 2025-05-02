package client.admin.view;

import client.admin.controller.AdminViewPaymentController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import shared.classes.Payment;

import java.util.Date;
import java.util.List;

public class AdminViewPaymentView {
    @FXML
    private TextField searchResTextField;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<Payment> paymentTableView;
    @FXML
    private TableColumn<Payment, String> invoiceIDColumn;
    @FXML
    private TableColumn<Payment, String> studentIDColumn;
    @FXML
    private TableColumn<Payment, String> studentNameColumn;
    @FXML
    private TableColumn<Payment, String> paymentDateColumn;
    @FXML
    private TableColumn<Payment, String> paymentTimeColumn;
    @FXML
    private TableColumn<Payment, String> paymentMethodColumn;
    @FXML
    private TableColumn<Payment, String> amountColumn;
    private AdminViewPaymentController controller;
    private ObservableList<Payment> paymentData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Table columns initialized successfully.");
        initializeController();

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.searchPayment(newValue);
        });
        refreshButton.setOnAction(event -> controller.loadPayments());
    }

    public void initializeTableColumns() {
        invoiceIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentID()));
        studentIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentID()));
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentName()));
        paymentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentDate().toString()));
        paymentTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPaymentTime())));
        paymentMethodColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaymentMethod()));
        amountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAmount())));
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing AdminViewPaymentController...");
        this.controller = new AdminViewPaymentController(this);
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] AdminViewPaymentController successfully created.");
    }

    public void updateTable(List<Payment> data) {
        paymentData.setAll(data); // Update dataset
        paymentTableView.setItems(null); // Force reset
        paymentTableView.setItems(paymentData); // Reload table data
        paymentTableView.refresh(); // Force UI refresh
        System.out.println("[CLIENT(Admin)] Payment data updated. New table size: " + paymentData.size());
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
