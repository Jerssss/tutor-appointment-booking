package shared.interfaces;

import shared.classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface AdminService extends Remote {
    List<Student> viewStudent() throws RemoteException;
    void addStudent(Student newStudent) throws RemoteException, SQLException;
    void modifyStudent(String studentID, String newPassword) throws RemoteException;
    List<Tutor> viewTutor() throws RemoteException;
    void addTutor(Tutor newTutor) throws RemoteException, SQLException;
    void modifyTutor(String tutorID, String newPassword) throws RemoteException;
    List<List<String>> viewSession() throws RemoteException;
    List<String> viewOtherSessionDetails(String sessionID) throws RemoteException;
    void addSession(TutorSession session) throws RemoteException, SQLException;
    void modifySession(String sessionID, String sessionMode, String sessionType, String numStudents, String maxStudents, String sessionPrice) throws RemoteException, SQLException;
    List<String> getEditableDetails(String sessionID) throws RemoteException;
    List<String> getAllTutorNames() throws RemoteException;
    List<String> getAllSubjectNames() throws RemoteException;
    List<String> getAllTutorIDs() throws RemoteException;
    List<String> getAllSubjectIDs() throws RemoteException;
    String getTutorID(String tutorName) throws RemoteException;
    String getTutorName(String tutorID) throws RemoteException;
    List<String> getAllSessionID() throws RemoteException;
    Map<LocalTime, Integer> getTutorSchedule(String tutorID, String date) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    List<String> viewOtherSubjectDetails(String subjectID) throws RemoteException;
    void addSubject(Subject subject) throws RemoteException, SQLException;
    String getSubjectID(String SubjectName) throws RemoteException;
    String getSubjectName(String SubjectID) throws RemoteException;
    void modifySubject(String subjectID, String academicLevel) throws RemoteException, SQLException;
    Map<LessonPlan, String> viewLessonPlan() throws RemoteException;
    LessonPlan addLessonPlan() throws RemoteException;
    LessonPlan modifyLessonPlan() throws RemoteException;
    List<Payment> viewPayment() throws RemoteException;

}
