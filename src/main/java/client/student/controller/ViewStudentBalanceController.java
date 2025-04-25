package client.student.controller;

import client.student.model.ViewStudentBalanceModel;
import client.student.view.ViewStudentBalanceView;
import shared.classes.BalanceDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.SessionManager;

import java.rmi.RemoteException;
import java.util.List;

public class ViewStudentBalanceController {
    private final ViewStudentBalanceModel model;
    private final ViewStudentBalanceView view;

    public ViewStudentBalanceController(ViewStudentBalanceModel model, ViewStudentBalanceView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
    }

    public void refreshTable() {
        try {
            String studentId = SessionManager.getCurrentUserId();
            System.out.println("[CLIENT] Fetching balance details for student ID: " + studentId);

            if (studentId == null) {
                view.showErrorAlert("Session Error", "No active session found");
                return;
            }

            // Fetch balance details for the table
            List<BalanceDetails> balanceDetails = model.fetchBalanceDetails(studentId);
            ObservableList<BalanceDetails> observableBalanceDetails = FXCollections.observableArrayList(balanceDetails);
            view.updateTable(observableBalanceDetails);

            // Fetch and display the current balance
            double balance = model.fetchStudentBalance(studentId);
            view.updateBalanceDisplay(balance);

        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error", "Failed to load balance details: " + e.getMessage());
        }
    }
}