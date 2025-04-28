package client.tutor.model;

import client.StudentTutorClient;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;

import java.rmi.RemoteException;

public class TutorViewMorePopUpModel {
    private final TutorService tutorService;

    public TutorViewMorePopUpModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public TutorSession fetchSessionDetails(String sessionID) throws RemoteException {
        return (TutorSession) tutorService.getSessionDetails(sessionID);
    }
}