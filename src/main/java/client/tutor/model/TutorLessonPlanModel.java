package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.LessonPlan;
import shared.classes.Student;
import shared.interfaces.TutorService;

import java.util.List;

public class TutorLessonPlanModel {
    private TutorService tutorService;

    public TutorLessonPlanModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public List<LessonPlan> fetchLessonPlans() {
        try {
            return tutorService.viewLessonPlan();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch lesson plan: " + e.getMessage());
            return null;
        }
    }

    public boolean addNewLessonPlan(LessonPlan newLessonPlan) {
        try {
            tutorService.addLessonPlan(newLessonPlan);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to add lesson plan via RMI: " + e.getMessage());
            return false;
        }
    }

    public boolean modifyLessonPlanObjectives(String id, String newObjectives, String newTopicsCovered) {
        try {
            tutorService.modifyLessonPlan(id, newObjectives, newTopicsCovered);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to modify objectives via RMI: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteLessonPlan(String lessonPlanID) {
        try {
            tutorService.deleteLessonPlan(lessonPlanID);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to delete lesson plan via RMI: " + e.getMessage());
            return false;
        }
    }
}
