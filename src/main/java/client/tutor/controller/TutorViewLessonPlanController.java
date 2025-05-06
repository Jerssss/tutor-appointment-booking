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
    private ObservableList <LessonPlan> archivedLessonPlanData = FXCollections.observableArrayList(); // For archived lesson plans
    private final String tutorID;

    public TutorViewLessonPlanController(TutorViewLessonPlanView view, String tutorID) {
        this.view = view;
        this.model = new TutorLessonPlanModel();
        this.tutorID = tutorID;
        loadLessonPlans(tutorID);
        loadArchivedLessonPlans(tutorID); // Load archived lesson plans
    }

    public String getLoggedInTutorID() {
        return tutorID;
    }

    public void loadLessonPlans(String tutorID) {
        System.out.println("[CLIENT] loadLessonPlans() method called.");

        List<LessonPlan> lessonPlans = model.fetchLessonPlansByTutor(tutorID);

        if (lessonPlans != null) {
            Platform.runLater(() -> {
                lessonPlanData.setAll(lessonPlans);
                view.updateTable(lessonPlans);
                System.out.println("[CLIENT] Table updated with " + lessonPlans.size() + " lesson plans.");
            });
        } else {
            System.err.println("[ERROR] Failed to load lesson plans.");
        }
    }

    public void loadArchivedLessonPlans(String tutorID) {
        System.out.println("[CLIENT] loadArchivedLessonPlans() method called.");

        List<LessonPlan> archivedLessonPlans = model.fetchArchivedLessonPlansByTutor(tutorID); // Fetch archived lesson plans

        if (archivedLessonPlans != null) {
            Platform.runLater(() -> {
                archivedLessonPlanData.setAll(archivedLessonPlans);
                view.updateArchivedTable(archivedLessonPlans);
                System.out.println("[CLIENT] Archived table updated with " + archivedLessonPlans.size() + " archived lesson plans.");
            });
        } else {
            System.err.println("[ERROR] Failed to load archived lesson plans.");
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
                                lessonPlan.getSubjectName().toLowerCase().contains(lowerCaseQuery) ||
                                lessonPlan.getObjectives().toLowerCase().contains(lowerCaseQuery) ||
                                lessonPlan.getTopicsCovered().toLowerCase().contains(lowerCaseQuery)
                )
                .collect(Collectors.toList());

        view.updateTable(FXCollections.observableArrayList(filteredList));
    }

    public void deleteLessonPlan(LessonPlan lessonPlan) {
        if (lessonPlan == null) {
            System.err.println("[WARN] No lesson plan selected for deletion.");
            return;
        }

        boolean success = model.deleteLessonPlan(lessonPlan.getLessonPlanID());

        if (success) {
            System.out.println("[CLIENT] Archived lesson plan: " + lessonPlan.getLessonPlanID());

            // Remove from active list and refresh table
            lessonPlanData.remove(lessonPlan);
            view.updateTable(lessonPlanData);

            // Also refresh archived table to reflect new entry
            loadArchivedLessonPlans(getLoggedInTutorID());
        } else {
            System.err.println("[CLIENT ERROR] Could not mark lesson plan as archived.");
        }
    }
}