package client.admin.model;

import client.AdminClient;
import shared.classes.Payment;
import shared.classes.Student;
import shared.interfaces.AdminService;

import java.util.List;

public class AdminPaymentModel {
    private AdminService adminService;

    public AdminPaymentModel() {
        this.adminService = AdminClient.getAdminProcessors(); // Get RMI instance
    }

    public List<Payment> fetchPayments() {
        try {
            return adminService.viewPayment();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch payments: " + e.getMessage());
            return null;
        }
    }
}
