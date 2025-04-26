package client.student.model;


import client.StudentTutorClient;
import shared.classes.LessonPlan;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewLessonPlanModel {
    private StudentService studentService;

    public ViewLessonPlanModel(StudentService studentService) {
        this.studentService = StudentTutorClient.getStudentService();
    }

    public List<LessonPlan> fetchHighSchoolLessonPlans() throws RemoteException {
        // Fetch high school lesson plans from the service
        return studentService.viewHighSchoolLessonPlan();
    }

    public List<LessonPlan> fetchCollegeLessonPlans() throws RemoteException {
        // Fetch college lesson plans from the service
        return studentService.viewCollegeLessonPlan();
    }
}