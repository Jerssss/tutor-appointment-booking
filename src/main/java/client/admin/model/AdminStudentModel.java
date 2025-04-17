package client.admin.model;

import server.services.AdminServiceImpl;
import shared.classes.Student;
import shared.interfaces.AdminService;
import client.AdminClient;

import java.util.List;

public class AdminStudentModel {

    private AdminService adminService;

    public AdminStudentModel() {
        this.adminService = AdminClient.getAdminProcessors(); // Get RMI instance
    }

    public List<Student> fetchStudents() {
        try {
            return adminService.viewStudent();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch terminals: " + e.getMessage());
            return null;
        }
    }

    public boolean addNewStudent(Student newStudent) {
        try {
            adminService.addStudent(newStudent);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add student via RMI: " + e.getMessage());
            return false;
        }
    }


}
