package client.student.controller;

import client.student.model.ViewPaymentHistoryModel;
import client.student.view.ViewPaymentHistoryView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.PaymentDetails;
import shared.classes.SessionManager;

import java.rmi.RemoteException;
import java.util.List;

public class ViewPaymentHistoryController {
    private final ViewPaymentHistoryModel model;
    private final ViewPaymentHistoryView view;

    public ViewPaymentHistoryController(ViewPaymentHistoryModel model, ViewPaymentHistoryView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this); // Set the controller in the view
    }

    public void refreshTable() {
        try {
            // Get student ID from session
            String studentIdStr = SessionManager.getCurrentUserId();
            System.out.println("[CLIENT] Fetching payment history for student ID: " + studentIdStr);
            if (studentIdStr == null) {
                view.showErrorAlert("Session Error", "No active session found");
                return;
            }

            // Parse to integer (assuming your student IDs are numeric)
            int studentId;
            try {
                studentId = Integer.parseInt(studentIdStr);
            } catch (NumberFormatException e) {
                view.showErrorAlert("Invalid ID", "Student ID must be numeric");
                return;
            }

            // Fetch and display payments
            List<PaymentDetails> payments = model.fetchPaymentHistory(studentId);
            ObservableList<PaymentDetails> observablePayments = FXCollections.observableArrayList(payments);
            view.updateTable(observablePayments);

        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error", "Failed to load payments: " + e.getMessage());
        }
    }
}