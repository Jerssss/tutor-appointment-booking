package client.student.controller;

import client.student.model.CreateBookingModel;
import client.student.view.CreateBookingView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import shared.classes.Booking;
import shared.classes.TutorSession;
import java.rmi.RemoteException;
import java.util.List;

public class CreateBookingController {
    private CreateBookingModel model;
    private CreateBookingView view;

    public CreateBookingController(CreateBookingModel model, CreateBookingView view) {
        this.model = model;
        this.view = view;
    }

}