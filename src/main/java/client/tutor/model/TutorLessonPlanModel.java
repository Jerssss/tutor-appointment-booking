package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.LessonPlan;
import shared.interfaces.TutorService;

import java.rmi.RemoteException;
import java.util.List;

public class TutorLessonPlanModel {
    private TutorService tutorService;

    public TutorLessonPlanModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public List<LessonPlan> fetchLessonPlansByTutor(String tutorID) {
        try {
            return tutorService.viewLessonPlanByTutor(tutorID);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch lesson plans for tutor: " + e.getMessage());
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

    public List<String> fetchSubjectsByExpertise(String tutorID) {
        try {
            return tutorService.getSubjectsByTutorExpertise(tutorID);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch subjects for tutor: " + e.getMessage());
            return null;
        }
    }

    public LessonPlan getLessonPlanDetails(String subjectID) {
        try {
            return tutorService.getLessonPlanBySubjectID(subjectID);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch lesson plan details: " + e.getMessage());
            return null;
        }
    }
    public String getSubjectIDByName(String subjectName) throws RemoteException {
        return tutorService.getSubjectIDByName(subjectName); // Call the service method
    }
}