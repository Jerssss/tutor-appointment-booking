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
    void addSession(TutorSession session) throws RemoteException, SQLException;
    TutorSession modifySession() throws RemoteException;
    List<String> getAllTutorName() throws RemoteException;
    List<String> getAllSubjects() throws RemoteException;
    String getSubjectID(String subjectName) throws RemoteException;
    String getTutorID(String tutorName) throws RemoteException;
    List<String> getAllSessionID() throws RemoteException;
    Map<LocalTime, Integer> getTutorSchedule(String tutorID, String date) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    void addSubject(Subject subject) throws RemoteException, SQLException;
    Subject modifySubject() throws RemoteException;
    Map<LessonPlan, String> viewLessonPlan() throws RemoteException;
    LessonPlan addLessonPlan() throws RemoteException;
    LessonPlan modifyLessonPlan() throws RemoteException;
    List<Payment> viewPayment() throws RemoteException;

}
