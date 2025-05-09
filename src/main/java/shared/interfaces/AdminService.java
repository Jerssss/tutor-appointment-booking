package shared.interfaces;

import shared.classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface AdminService extends Remote {
    List<Student> viewStudent() throws RemoteException;
    void addStudent(Student newStudent) throws RemoteException, SQLException;
    void modifyStudent(String studentID, String newPassword) throws RemoteException;
    void removeStudent(Student student) throws RemoteException;
    List<Tutor> viewTutor() throws RemoteException;
    void addTutor(Tutor newTutor) throws RemoteException, SQLException;
    List<String> viewExpertise() throws RemoteException;
    void modifyTutor(String tutorID, String newPassword, String visibility) throws RemoteException;
    void removeTutor(Tutor tutor) throws RemoteException;
    List<TutorSession> viewSession() throws RemoteException;
    void addSession(TutorSession session) throws RemoteException, SQLException;
    void modifySession(TutorSession session) throws RemoteException, SQLException;
    int deleteSession(String sessionID) throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    void addSubject(Subject subject) throws RemoteException, SQLException;
    void modifySubject(String subjectID, String academicLevel, String subjectVisibility) throws RemoteException, SQLException;
    int deleteSubject(String subjectID) throws RemoteException, SQLIntegrityConstraintViolationException;
    List<LessonPlan> viewLessonPlan() throws RemoteException;
    void modifyLessonPlan(String lessonPlanID, String visibility) throws RemoteException, SQLException;
    List<Payment> viewPayment() throws RemoteException;
    void createPayment(Payment payment) throws RemoteException, SQLException;
    List<String> getStudentList() throws RemoteException;

}
