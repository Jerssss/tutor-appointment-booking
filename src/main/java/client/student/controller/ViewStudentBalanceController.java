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
        this.view.setController(this); // Set the controller in the view
    }

    public void refreshTable() {
        try {
            // Get student ID from session
            String studentIdStr = SessionManager.getCurrentUserId();
            System.out.println("[CLIENT] Fetching balance details for student ID: " + studentIdStr);
            if (studentIdStr == null) {
                view.showErrorAlert("Session Error", "No active session found");
                return;
            }

            // Parse to integer (assuming your student IDs are numeric)
            String studentId;
            try {
                studentId = studentIdStr;
            } catch (NumberFormatException e) {
                view.showErrorAlert("Invalid ID", "Student ID must be numeric");
                return;
            }

            // Fetch and display balance details
            List<BalanceDetails> balanceDetails = model.fetchBalanceDetails(studentId);
            ObservableList<BalanceDetails> observableBalanceDetails = FXCollections.observableArrayList(balanceDetails);
            view.updateTable(observableBalanceDetails);

        } catch (RemoteException e) {
            view.showErrorAlert("Connection Error", "Failed to load balance details: " + e.getMessage());
        }
    }
}