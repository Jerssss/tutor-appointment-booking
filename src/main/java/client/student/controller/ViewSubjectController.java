package client.student.controller;

import client.student.model.ViewSubjectModel;
import client.student.view.ViewSubjectView;
import shared.classes.Subject;

import java.rmi.RemoteException;
import java.util.List;

public class ViewSubjectController {
    private final ViewSubjectView view;
    private final ViewSubjectModel model;

    public ViewSubjectController(ViewSubjectView view, ViewSubjectModel model) {
        this.view = view;
        this.model = model;
    }

    public void refreshTable() {
        System.out.println("[CLIENT] Refreshing table...");
        try {
            // Fetch subjects from the model
            List<Subject> subjects = model.fetchSubjects();
            System.out.println("[CLIENT] Fetched " + subjects.size() + " subjects.");
            // Update the view with the fetched subjects
            view.updateTable(subjects);
        } catch (RemoteException e) {
            System.err.println("Error fetching subjects: " + e.getMessage());
        }
    }
}