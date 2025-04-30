package client.tutor.controller;

import client.tutor.model.TutorLessonPlanModel;
import shared.classes.LessonPlan;

public class TutorCreateLessonPlanController {
    private final TutorLessonPlanModel model;

    public TutorCreateLessonPlanController() {
        this.model = new TutorLessonPlanModel();
    }

    // Update the method to include subjectName
    public boolean addNewLessonPlan(String lessonPlanID, String subjectID, String subjectName, String objectives, String topicsCovered) {
        LessonPlan newLessonPlan = new LessonPlan(lessonPlanID, subjectID, subjectName, objectives, topicsCovered);
        return model.addNewLessonPlan(newLessonPlan);
    }
}