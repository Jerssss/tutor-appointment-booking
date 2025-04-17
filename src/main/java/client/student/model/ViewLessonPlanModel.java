package client.student.model;

import server.services.StudentServiceImpl;
import shared.classes.LessonPlan;
import shared.interfaces.StudentService;

import java.rmi.RemoteException;
import java.util.List;

public class ViewLessonPlanModel {
    private final StudentService service;

    public ViewLessonPlanModel(StudentService service) {
        this.service = service;
    }

    public List<LessonPlan> fetchHighSchoolLessonPlans() throws RemoteException {
        // Fetch high school lesson plans from the service
        return service.viewHighSchoolLessonPlan();
    }

    public List<LessonPlan> fetchCollegeLessonPlans() throws RemoteException {
        // Fetch college lesson plans from the service
        return service.viewCollegeLessonPlan();
    }
}