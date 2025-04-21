package client.tutor.controller;

import client.tutor.model.TutorLessonPlanModel;

public class TutorModifyLessonPlanController {
    private final TutorLessonPlanModel model;
    public TutorModifyLessonPlanController() {
        this.model = new TutorLessonPlanModel();
    }

    public boolean updateLessonPlanObjectives(String id, String newObjectives, String newTopicsCovered) {
        return model.modifyLessonPlanObjectives(id, newObjectives, newTopicsCovered);
    }
}
