package shared.interfaces;

import shared.classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface AdminService extends Remote {
    List<Student> viewStudent() throws RemoteException;
    void addStudent(Student newStudent) throws RemoteException, SQLException;
    void modifyStudent(String studentID, String newPassword) throws RemoteException;
    List<Tutor> viewTutor() throws RemoteException;
    void addTutor(Tutor newTutor) throws RemoteException, SQLException;
    void modifyTutor(String tutorID, String newPassword) throws RemoteException;
    List<List<String>> viewSession() throws RemoteException;
    TutorSession addSession() throws RemoteException;
    TutorSession modifySession() throws RemoteException;
    List<Subject> viewSubject() throws RemoteException;
    Subject addSubject() throws RemoteException;
    Subject modifySubject() throws RemoteException;
    List<LessonPlan> viewLessonPlan() throws RemoteException;
    LessonPlan addLessonPlan() throws RemoteException;
    LessonPlan modifyLessonPlan() throws RemoteException;
    List<Payment> viewPayment() throws RemoteException;

}
