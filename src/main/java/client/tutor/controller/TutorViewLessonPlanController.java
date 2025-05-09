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
    private ObservableList<LessonPlan> archivedLessonPlanData = FXCollections.observableArrayList();
    private final String tutorID;

    public TutorViewLessonPlanController(TutorViewLessonPlanView view, String tutorID) {
        this.view = view;
        this.model = new TutorLessonPlanModel();
        this.tutorID = tutorID;
        System.out.println("[CLIENT] Initializing TutorViewLessonPlanController for tutorID: " + tutorID);
        loadLessonPlans(tutorID);
        loadArchivedLessonPlans(tutorID);
    }

    public String getLoggedInTutorID() {
        return tutorID;
    }

    public void loadLessonPlans(String tutorID) {
        System.out.println("[CLIENT] loadLessonPlans() called for tutorID: " + tutorID);
        List<LessonPlan> lessonPlans = model.fetchLessonPlansByTutor(tutorID);

        if (lessonPlans != null) {
            Platform.runLater(() -> {
                lessonPlanData.setAll(lessonPlans);
                view.updateTable(lessonPlans);
                System.out.println("[CLIENT] Table updated with " + lessonPlans.size() + " lesson plans.");
            });
        } else {
            System.err.println("[CLIENT] Failed to load lesson plans for tutorID: " + tutorID);
            Platform.runLater(() -> {
                lessonPlanData.clear();
                view.updateTable(lessonPlanData);
            });
        }
    }

    public void loadArchivedLessonPlans(String tutorID) {
        System.out.println("[CLIENT] loadArchivedLessonPlans() called for tutorID: " + tutorID);
        List<LessonPlan> archivedLessonPlans = model.fetchArchivedLessonPlansByTutor(tutorID);

        if (archivedLessonPlans != null) {
            Platform.runLater(() -> {
                archivedLessonPlanData.setAll(archivedLessonPlans);
                view.updateArchivedTable(archivedLessonPlans);
                System.out.println("[CLIENT] Archived table updated with " + archivedLessonPlans.size() + " archived lesson plans.");
            });
        } else {
            System.err.println("[CLIENT] Failed to load archived lesson plans for tutorID: " + tutorID);
            Platform.runLater(() -> {
                archivedLessonPlanData.clear();
                view.updateArchivedTable(archivedLessonPlanData);
            });
        }
    }

    public void refreshTables() {
        System.out.println("[CLIENT] refreshTables() called for tutorID: " + tutorID);
        loadLessonPlans(tutorID);
        loadArchivedLessonPlans(tutorID);
    }

    public void searchLessonPlan(String query) {
        System.out.println("[CLIENT] Searching lesson plans with query: " + query);
        if (lessonPlanData.isEmpty()) {
            view.updateTable(lessonPlanData);
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
            System.err.println("[CLIENT] No lesson plan selected for deletion.");
            return;
        }

        boolean success = model.deleteLessonPlan(lessonPlan.getLessonPlanID());

        if (success) {
            System.out.println("[CLIENT] Archived lesson plan: " + lessonPlan.getLessonPlanID());
            Platform.runLater(() -> {
                lessonPlanData.remove(lessonPlan);
                view.updateTable(lessonPlanData);
                loadArchivedLessonPlans(tutorID); // Refresh archived table
            });
        } else {
            System.err.println("[CLIENT] Could not mark lesson plan as archived: " + lessonPlan.getLessonPlanID());
        }
    }
}