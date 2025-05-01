package client.tutor.model;

import client.StudentTutorClient;
import shared.interfaces.TutorService;
import shared.classes.LessonPlan;

import java.util.List;

public class TutorCreateLessonPlanModel {
    private final TutorService tutorService;

    public TutorCreateLessonPlanModel(TutorService tutorService) {
        this.tutorService = StudentTutorClient.getTutorService();
    }
}