package client.tutor.controller;

import client.tutor.model.TutorViewMorePopUpModel;
import client.tutor.view.TutorViewMorePopUp;
import shared.classes.TutorSession;

import java.rmi.RemoteException;

public class TutorViewMoreListPopUpController {
    private final TutorViewMorePopUp view;
    private final TutorViewMorePopUpModel model;

    public TutorViewMoreListPopUpController(TutorViewMorePopUp view) {
        this.view = view;
        this.model = new TutorViewMorePopUpModel();
    }
}