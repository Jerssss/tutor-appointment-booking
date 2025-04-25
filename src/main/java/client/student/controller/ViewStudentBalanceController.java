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
            List<BalanceDetails> details = model.fetchBalanceDetails(studentId);
            view.updateTable(FXCollections.observableArrayList(details));
            view.updateBalanceDisplay(model.getCurrentBalance(studentId));
        } catch (RemoteException e) {
            view.showErrorAlert("Error", "Failed to refresh: " + e.getMessage());
        }
    }

    public double getCurrentBalance() throws RemoteException {
        return model.getCurrentBalance(SessionManager.getCurrentUserId());
    }
}