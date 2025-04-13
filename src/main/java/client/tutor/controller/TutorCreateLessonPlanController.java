package client.tutor.controller;

import client.tutor.model.TutorCreateLessonPlanModel;
import shared.classes.LessonPlan;

public class TutorCreateLessonPlanController {
    private final TutorCreateLessonPlanModel model;

    public TutorCreateLessonPlanController(TutorCreateLessonPlanModel model) {
        this.model = model;
    }

    public void createLessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) {
        LessonPlan plan = model.createLessonPlan(lessonPlanID, subjectID, objectives, topicsCovered);
        if (plan != null) {
            System.out.println("[SUCCESS] Lesson plan created: " + plan.getLessonPlanID());
        } else {
            System.out.println("[ERROR] Failed to create lesson plan.");
        }
    }

//    // For console testing
//    public void start() {
//        // Generate example values
//        String lessonPlanID = "LP416";
//        String subjectID = "HS 111";
//        String objectives = "Familiarize students with basic computing concepts and more";
//        String topicsCovered = "Classes, Inheritance";
//
//        createLessonPlan(lessonPlanID, subjectID, objectives, topicsCovered);
//    }
}