package client.student.controller;

import client.student.model.CreateBookingModel;
import client.student.view.CreateBookingView;
import shared.classes.*;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingController {
    private final CreateBookingModel model;
    private final CreateBookingView view;

    public CreateBookingController(CreateBookingModel model, CreateBookingView view) {
        this.model = model;
        this.view = view;
    }

    public void refreshTable() throws RemoteException {
        List<TutorSession> sessions = model.fetchSessions();
        view.updateTable(sessions);
    }
}