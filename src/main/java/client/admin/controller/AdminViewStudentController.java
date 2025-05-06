package client.admin.controller;

import client.admin.model.AdminStudentModel;
import client.admin.view.AdminViewStudentView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Student;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AdminViewStudentController {
    private final AdminViewStudentView view;
    private final AdminStudentModel model;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<Student> archivedStudentData = FXCollections.observableArrayList();

    public AdminViewStudentController(AdminViewStudentView view) {
        this.view = view;
        this.model = new AdminStudentModel();

        loadStudents();
        loadArchivedStudents();
    }

    public void loadStudents() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] loadStudents() method called.");

        List<Student> students = model.loadStudents();

        if (students != null) {
            Platform.runLater(() -> {
                studentData.setAll(students); // Update observable list
                view.updateTable(students);
                System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Table updated with " + students.size() + " terminals.");
            });
        } else {
            System.err.println("[ERROR] Failed to load students.");
        }
    }

    public void loadArchivedStudents() {
        System.out.println("[CLIENT] loadArchivedStudents() method called.");

        List<Student> students = model.loadArchivedStudents();

        if (students != null) {
            Platform.runLater(() -> {
                archivedStudentData.setAll(students); // Update observable list
                view.updateArchiveTable(students);
                System.out.println("[CLIENT] Table updated with " + students.size() + " terminals.");
            });
        } else {
            System.err.println("[ERROR] Failed to load terminals.");
        }
    }

    public void searchStudents(String query) {
        if (studentData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(studentData); // Reset table to original data
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Student> filteredList = studentData.stream()
                .filter(student ->
                        student.getUserID().toLowerCase().contains(lowerCaseQuery) ||
                                student.getFirstName().toLowerCase().contains(lowerCaseQuery) ||
                                student.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                                String.valueOf(student.getPhoneNumber()).toLowerCase().contains(lowerCaseQuery) ||
                                student.getEmail().toLowerCase().contains(lowerCaseQuery) ||
                                student.getAcademicLevel().toLowerCase().contains(lowerCaseQuery) ||
                                String.valueOf(student.getBalance()).toLowerCase().contains(lowerCaseQuery) // <-- wrap this whole thing
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    public void removeStudent(Student student) {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "]Remove student in controller is set");
        model.removeStudent(student);
    }
}
