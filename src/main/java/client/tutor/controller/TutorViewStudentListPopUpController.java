package client.tutor.controller;

import client.tutor.model.TutorViewStudentListPopUpModel;
import client.tutor.view.TutorViewStudentListPopUp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Student;
import java.util.List;
import java.util.stream.Collectors;

public class TutorViewStudentListPopUpController {
    private final TutorViewStudentListPopUp view;
    private final TutorViewStudentListPopUpModel model;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();


    public TutorViewStudentListPopUpController(TutorViewStudentListPopUp view, String sessionID) {
        this.view = view;
        this.model = new TutorViewStudentListPopUpModel();
        loadStudentsData(sessionID);
    }

    private void loadStudentsData(String sessionID) {
        List<Student> students = model.fetchStudents(sessionID);
        if (students != null) {
            Platform.runLater(() -> {
                studentData.setAll(students);
                view.updateTable(students);
            });
        } else {
            System.out.println("error");
        }
    }
    public void searchStudents(String query) {
        if (studentData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(studentData);
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Student> filteredList = studentData.stream()
                .filter(student ->
                        student.getUserID().toLowerCase().contains(lowerCaseQuery) ||
                                student.getFirstName().toLowerCase().contains(lowerCaseQuery) ||
                                student.getLastName().toLowerCase().contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    public void loadStudents(String sessionID) {
        System.out.println("[CLIENT] loadSessions() method called.");

        List<Student> students = model.fetchStudents(sessionID);

        if (students != null) {
            Platform.runLater(() -> {
                studentData.setAll(students); // Update observable list
                view.updateTable(students);
                System.out.println("[CLIENT] Table updated with " + students.size() + " sessions.");
            });
        } else {
            System.err.println("[ERROR] Failed to load sessions.");
        }
    }
}
