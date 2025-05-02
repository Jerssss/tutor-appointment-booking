package client.admin.controller;

import client.admin.model.AdminTutorModel;
import client.admin.view.AdminViewTutorView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.Tutor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AdminViewTutorController {
    private final AdminViewTutorView view;
    private final AdminTutorModel model;
    private ObservableList<Tutor> tutorData = FXCollections.observableArrayList();
    public AdminViewTutorController(AdminViewTutorView view) {
        this.view = view;
        this.model = new AdminTutorModel();

        loadTutors();
    }

    public void loadTutors() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] loadTutors() method called.");

        List<Tutor> tutors = model.fetchTutors();

        if (tutors != null) {
            Platform.runLater(() -> {
                tutorData.setAll(tutors); // Update observable list
                view.updateTable(tutors);
                System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Table updated with " + tutors.size() + " terminals.");
            });
        } else {
            System.err.println("[ERROR] Failed to load tutor.");
        }
    }

    public void searchTutors(String query) {
        if (tutorData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(tutorData); // Reset table to original data
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<Tutor> filteredList = tutorData.stream()
                .filter(tutor ->
                        tutor.getUserID().toLowerCase().contains(lowerCaseQuery) ||
                                tutor.getFirstName().toLowerCase().contains(lowerCaseQuery) ||
                                tutor.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                                String.valueOf(tutor.getPhoneNumber()).toLowerCase().contains(lowerCaseQuery) ||
                                tutor.getEmail().toLowerCase().contains(lowerCaseQuery) ||
                                tutor.getExpertise().toLowerCase().contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    public void removeTutor(Tutor tutor) {
        model.removeTutor(tutor);
    }
}
