package client.tutor.model;


import client.StudentTutorClient;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;


import java.rmi.RemoteException;
import java.util.List;


public class TutorViewSessionListModel {
    private final TutorService tutorService;


    public TutorViewSessionListModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }


    public List<TutorSession> getSessionList() {
        try {
            return tutorService.viewSessionList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
