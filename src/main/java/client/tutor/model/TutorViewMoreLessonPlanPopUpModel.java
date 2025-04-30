package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.LessonPlan;
import shared.interfaces.TutorService;

import java.rmi.RemoteException;

public class TutorViewMoreLessonPlanPopUpModel {
    private final TutorService tutorService;

    public TutorViewMoreLessonPlanPopUpModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public LessonPlan fetchLessonPlanDetails(String lessonPlanID) throws RemoteException {
        LessonPlan lessonPlan = tutorService.getLessonPlanDetails(lessonPlanID);
        if (lessonPlan != null) {
            System.out.println("Fetched Lesson Plan: " + lessonPlan);
        } else {
            System.out.println("No Lesson Plan found for ID: " + lessonPlanID);
        }
        return lessonPlan;
    }
}