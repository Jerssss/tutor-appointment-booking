package client.student.controller;

import client.student.model.ViewLessonPlanModel;
import client.student.view.ViewLessonPlanView;
import shared.classes.LessonPlan;

import java.rmi.RemoteException;
import java.util.List;

public class ViewLessonPlanController {
    private final ViewLessonPlanView view;
    private final ViewLessonPlanModel model;

    public ViewLessonPlanController(ViewLessonPlanView view, ViewLessonPlanModel model) {
        this.view = view;
        this.model = model;
    }

    public void refreshTable() {
        System.out.println("[CLIENT] Refreshing lesson plans table...");
        try {
            // Fetch high school lesson plans from the model
            List<LessonPlan> highSchoolLessonPlans = model.fetchHighSchoolLessonPlans();
            // Fetch college lesson plans from the model
            List<LessonPlan> collegeLessonPlans = model.fetchCollegeLessonPlans();
            System.out.println("[CLIENT] Fetched " + highSchoolLessonPlans.size() + " high school lesson plans.");
            System.out.println("[CLIENT] Fetched " + collegeLessonPlans.size() + " college lesson plans.");
            // Update the view with the fetched lesson plans
            view.updateTable(highSchoolLessonPlans, collegeLessonPlans);
        } catch (RemoteException e) {
            System.err.println("Error fetching lesson plans: " + e.getMessage());
        }
    }
}