package client.tutor.model;

import shared.interfaces.TutorService;
import shared.classes.LessonPlan;
import server.database.DatabaseConnection;

public class TutorCreateLessonPlanModel {
    private final TutorService tutorService;

    public TutorCreateLessonPlanModel(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    public LessonPlan createLessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) {
        try {
            return tutorService.createLessonPlan(lessonPlanID, subjectID, objectives, topicsCovered);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}