package client.admin.model;

import client.AdminClient;
import shared.classes.Student;
import shared.classes.Tutor;
import shared.interfaces.AdminService;

import java.util.List;

public class AdminTutorModel {
    private final AdminService adminService;

    public AdminTutorModel() {
        this.adminService = AdminClient.getAdminService(); // Get RMI instance
    }

    public List<Tutor> fetchTutors() {
        try {
            return adminService.viewTutor();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch tutors: " + e.getMessage());
            return null;
        }
    }

    public boolean addNewTutor(Tutor newTutor) {
        try {
            adminService.addTutor(newTutor);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add tutor via RMI: " + e.getMessage());
            return false;
        }
    }

    public boolean modifyTutorPassword(String id, String newPassword) {
        try {
            adminService.modifyTutor(id, newPassword);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to modify tutor via RMI: " + e.getMessage());
            return false;
        }
    }

    public boolean removeTutor(Tutor tutor) {
        try {
            adminService.removeTutor(tutor);
            System.out.println("Remove student in model is set");
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add student via RMI: " + e.getMessage());
            return false;
        }
    }
}
