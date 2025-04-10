package shared.interfaces;

import shared.classes.LessonPlan;
import shared.classes.Student;
import shared.classes.TutorSession;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TutorService extends Remote {
    /*
    TutorSession viewSessionList();
    TutorSession modifySession();
    Student viewStudentList();
    LessonPlan viewLessonPlan();
    LessonPlan modifyLessonPlan();
     */
    LessonPlan createLessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) throws RemoteException;}
