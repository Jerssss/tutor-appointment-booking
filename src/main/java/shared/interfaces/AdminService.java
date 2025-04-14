package shared.interfaces;

import shared.classes.*;

import java.rmi.Remote;
import java.util.List;

public interface AdminService extends Remote {
    List<Student> viewStudent();
    Student addStudent();
    Student modifyStudent ();
    Tutor viewTutor();
    Tutor addTutor();
    List<TutorSession> viewSession();
    TutorSession addSession();
    TutorSession modifySession();
    List<Subject> viewSubject();
    Subject addSubject();
    Subject modifySubject();
    List<LessonPlan> viewLessonPlan();
    LessonPlan addLessonPlan();
    LessonPlan modifyLessonPlan();
    List<Payment> viewPayment();

}
