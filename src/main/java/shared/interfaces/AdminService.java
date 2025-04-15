package shared.interfaces;

import shared.classes.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AdminService extends Remote {
    List<Student> viewStudent() throws RemoteException;
    Student addStudent() throws RemoteException;
    Student modifyStudent() throws RemoteException;
    Tutor viewTutor() throws RemoteException;
    Tutor addTutor() throws RemoteException;
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
