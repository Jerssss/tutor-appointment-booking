package client.admin.model;

import client.AdminClient;
import shared.classes.Payment;
import shared.interfaces.AdminService;

import java.util.Collections;
import java.util.List;

public class AdminPaymentModel {
    private final AdminService adminService;

    public AdminPaymentModel() {
        this.adminService = AdminClient.getAdminService(); // Consistent naming with AdminClient
        if (this.adminService == null) {
            System.err.println("[ERROR] AdminService is null in AdminPaymentModel constructor.");
        }
    }

    public List<Payment> fetchPayments() {
        try {
            if (adminService != null) {
                return adminService.viewPayment();
            } else {
                System.err.println("[ERROR] Cannot fetch payments. AdminService is null.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception while fetching payments: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> getStudentList(){
        try {
            if (adminService != null) {
                return adminService.getStudentList();
            } else {
                System.err.println("[ERROR] Cannot fetch student list. AdminService is null.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Exception while fetching payments: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean addNewPayment(Payment newPayment) {
        try {
            adminService.createPayment(newPayment);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to create payment via RMI: " + e.getMessage());
            return false;
        }
    }
}
