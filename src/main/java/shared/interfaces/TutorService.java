package shared.interfaces;


import shared.classes.LessonPlan;
import shared.classes.Student;
import shared.classes.TutorSession;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;


public interface TutorService extends Remote {
    void addLessonPlan(LessonPlan newLessonPlan) throws RemoteException, SQLException;
    void modifyLessonPlan(String lessonPlanID, String newObjectives, String newTopicsCovered) throws RemoteException;
    List<LessonPlan> viewLessonPlanByTutor(String tutorID) throws RemoteException;
    List<Student> getStudentsBySession(String sessionID) throws RemoteException;
    List<TutorSession> viewSessionList(String tutorID) throws RemoteException;
    void deleteLessonPlan(String lessonPlanID) throws RemoteException;
    TutorSession getSessionDetails(String sessionID) throws RemoteException;
    LessonPlan getLessonPlanDetails(String lessonPlanID) throws RemoteException;
    List<String> getSubjectsByTutorExpertise(String tutorID) throws RemoteException;
    LessonPlan getLessonPlanBySubjectID(String subjectID) throws RemoteException;
    String getSubjectIDByName(String subjectID) throws RemoteException;
}