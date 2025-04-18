package shared.interfaces;


import shared.classes.LessonPlan;
import shared.classes.Student;
import shared.classes.TutorSession;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface TutorService extends Remote {
    /*
    TutorSession viewSessionList();
    TutorSession modifySession();
    Student viewStudentList();
    LessonPlan viewLessonPlan();
    LessonPlan modifyLessonPlan();
     */
    List<TutorSession> viewSessionList() throws RemoteException;
    LessonPlan createLessonPlan(String lessonPlanID, String subjectID, String objectives, String topicsCovered) throws RemoteException;}
