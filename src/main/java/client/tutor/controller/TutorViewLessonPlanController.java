package client.tutor.controller;

import client.tutor.model.TutorLessonPlanModel;
import client.tutor.view.TutorViewLessonPlanView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.classes.LessonPlan;

import java.util.List;
import java.util.stream.Collectors;

public class TutorViewLessonPlanController {
    private final TutorViewLessonPlanView view;
    private final TutorLessonPlanModel model;
    private ObservableList<LessonPlan> lessonPlanData = FXCollections.observableArrayList();

    public TutorViewLessonPlanController(TutorViewLessonPlanView view) {
        this.view = view;
        this.model = new TutorLessonPlanModel();

        loadLessonPlans();
    }

    public void loadLessonPlans() {
        System.out.println("[CLIENT] loadStudents() method called.");

        List<LessonPlan> lessonPLan = model.fetchLessonPlans();

        if (lessonPLan != null) {
            Platform.runLater(() -> {
                lessonPlanData.setAll(lessonPLan); // Update observable list
                view.updateTable(lessonPLan);
                System.out.println("[CLIENT] Table updated with " + lessonPLan.size() + " terminals.");
            });
        } else {
            System.err.println("[ERROR] Failed to load terminals.");
        }
    }

    public void searchLessonPlan(String query) {
        if (lessonPlanData.isEmpty()) {
            return;
        }

        if (query == null || query.trim().isEmpty()) {
            view.updateTable(lessonPlanData);
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        List<LessonPlan> filteredList = lessonPlanData.stream()
                .filter(lessonPlan ->
                        lessonPlan.getLessonPlanID().toLowerCase().contains(lowerCaseQuery) ||
                                lessonPlan.getSubjectID().toLowerCase().contains(lowerCaseQuery) ||
                                lessonPlan.getObjectives().toLowerCase().contains(lowerCaseQuery) ||
                                lessonPlan.getTopicsCovered().toLowerCase().contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    public void loadLessonPlan() {
        System.out.println("[CLIENT] loadSessions() method called.");

        List<LessonPlan> lessonPlans = model.fetchLessonPlans();

        if (lessonPlans != null) {
            Platform.runLater(() -> {
                lessonPlanData.setAll(lessonPlans); // Update observable list
                view.updateTable(lessonPlans);
                System.out.println("[CLIENT] Table updated with " + lessonPlans.size() + " sessions.");
            });
        } else {
            System.err.println("[ERROR] Failed to load sessions.");
        }
    }

    public void deleteLessonPlan(LessonPlan lessonPlan) {
        if (lessonPlan == null) {
            System.err.println("[WARN] No lesson plan selected for deletion.");
            return;
        }

        boolean success = model.deleteLessonPlan(lessonPlan.getLessonPlanID());

        if (success) {
            System.out.println("[CLIENT] Deleted lesson plan: " + lessonPlan.getLessonPlanID());
            lessonPlanData.remove(lessonPlan);
            view.updateTable(lessonPlanData);
        } else {
            System.err.println("[CLIENT ERROR] Could not delete lesson plan.");
        }
    }
}
