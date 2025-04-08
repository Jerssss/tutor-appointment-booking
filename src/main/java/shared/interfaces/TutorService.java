package shared.interfaces;

import shared.classes.LessonPlan;
import shared.classes.Student;
import shared.classes.TutorSession;

public interface TutorService {
    TutorSession viewSessionList();
    TutorSession modifySession();
    Student viewStudentList();
    LessonPlan viewLessonPlan();
    LessonPlan modifyLessonPlan();
    LessonPlan createLessonPlan();
}
