package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.LessonPlan;
import shared.interfaces.TutorService;

import java.rmi.RemoteException;
import java.util.Date;

public class TutorViewMoreLessonPlanPopUpModel {
    private final TutorService tutorService;

    public TutorViewMoreLessonPlanPopUpModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public LessonPlan fetchLessonPlanDetails(String lessonPlanID) throws RemoteException {
        LessonPlan lessonPlan = tutorService.getLessonPlanDetails(lessonPlanID);
        if (lessonPlan != null) {
            System.out.println("[CLIENT | "+ new Date()+ "] Fetched Lesson Plan: " + lessonPlan);
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No Lesson Plan found for ID: " + lessonPlanID);
        }
        return lessonPlan;
    }
}