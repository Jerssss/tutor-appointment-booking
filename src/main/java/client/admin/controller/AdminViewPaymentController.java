package client.admin.controller;

import client.admin.model.AdminPaymentModel;
import client.admin.view.AdminViewPaymentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Payment;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AdminViewPaymentController {
    private final AdminViewPaymentView view;
    private final AdminPaymentModel model;
    private ObservableList<Payment> paymentData = FXCollections.observableArrayList();
    public AdminViewPaymentController(AdminViewPaymentView view) {
        this.view = view;
        this.model = new AdminPaymentModel();

        loadPayments();
    }

    public void loadPayments() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] loadPayments() method called.");

        List<Payment> payments = model.fetchPayments();

        if (payments != null) {
            Platform.runLater(() -> {
                paymentData.setAll(payments); // Update observable list
                view.updateTable(payments);
                System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Table updated with " + payments.size() + " terminals.");
            });
        } else {
            System.err.println("[ERROR] Failed to load payment.");
        }
    }

    public void searchPayment(String query) {
        if (paymentData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(paymentData); // Reset table to original data
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Payment> filteredList = paymentData.stream()
                .filter(payment ->
                        payment.getPaymentID().toLowerCase().contains(lowerCaseQuery) ||
                                payment.getStudentID().toLowerCase().contains(lowerCaseQuery) ||
                                payment.getStudentName().toLowerCase().contains(lowerCaseQuery) ||
                                payment.getPaymentDate().toString().contains(lowerCaseQuery) ||
                                String.valueOf(payment.getPaymentTime()).toLowerCase().contains(lowerCaseQuery) ||
                                payment.getPaymentMethod().toLowerCase().contains(lowerCaseQuery) ||
                                String.valueOf(payment.getAmount()).toLowerCase().contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }
}
