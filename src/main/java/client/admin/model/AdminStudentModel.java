package client.admin.model;

import shared.classes.Student;
import shared.interfaces.AdminService;
import client.AdminClient;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminStudentModel {

    private AdminService adminService;

    public AdminStudentModel() {
        this.adminService = AdminClient.getAdminService();// Get RMI instance
    }

    public List<Student> fetchStudents() {
        try {
            return adminService.viewStudent();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch students: " + e.getMessage());
            return null;
        }
    }

    public List<Student> loadStudents() {
        try {
            List<Student> temp = adminService.viewStudent();
            List<Student> students = new ArrayList<>();
            for (Student s: temp) {
                if (s.getVisibility().equals("Available")) {
                    students.add(s);
                }
            }
            return students;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch students: " + e.getMessage());
            return null;
        }
    }

    public List<Student> loadArchivedStudents() {
        try {
            List<Student> temp = adminService.viewStudent();
            List<Student> students = new ArrayList<>();
            for (Student s: temp) {
                if (s.getVisibility().equals("Archived")) {
                    students.add(s);
                }
            }
            return students;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch students: " + e.getMessage());
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

    public boolean modifyStudentPassword(String id, String newPassword, String visibility) {
        try {
            adminService.modifyStudent(id, newPassword, visibility);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to modify student via RMI: " + e.getMessage());
            return false;
        }
    }

    public boolean removeStudent(Student student) {
        try {
            adminService.removeStudent(student);
            System.out.println("[ADMIN CLIENT | " + new Date() + "]Remove student in model is set");
            return true;
        } catch ( SQLIntegrityConstraintViolationException e){
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add student via RMI: " + e.getMessage());
            return false;
        }
    }
}
