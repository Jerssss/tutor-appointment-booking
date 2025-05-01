package client.tutor.controller;

import client.tutor.model.TutorLessonPlanModel;
import shared.classes.LessonPlan;

import java.rmi.RemoteException;
import java.util.List;

public class TutorCreateLessonPlanController {
    private final TutorLessonPlanModel model;

    public TutorCreateLessonPlanController() {
        this.model = new TutorLessonPlanModel();
    }

    public boolean addNewLessonPlan(String lessonPlanID, String subjectID, String subjectName, String objectives, String topicsCovered) {
        LessonPlan newLessonPlan = new LessonPlan(lessonPlanID, subjectID, subjectName, objectives, topicsCovered);
        return model.addNewLessonPlan(newLessonPlan);
    }

    public List<String> fetchSubjectsByExpertise(String tutorID) {
        return model.fetchSubjectsByExpertise(tutorID);
    }

    public LessonPlan getLessonPlanDetails(String subjectID) {
        return model.getLessonPlanDetails(subjectID);
    }

    public String getSubjectIDByName(String subjectName) throws RemoteException {
        return model.getSubjectIDByName(subjectName); // Call the model method
    }
}