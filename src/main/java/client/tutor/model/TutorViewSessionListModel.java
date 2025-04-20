package client.tutor.model;


import client.StudentTutorClient;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import java.util.List;


public class TutorViewSessionListModel {
    private final TutorService tutorService;


    public TutorViewSessionListModel() {
        this.tutorService = StudentTutorClient.getTutorService();
    }

    public List<TutorSession> fetchSessions() {
        try {
            return tutorService.viewSessionList();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch sessions: " + e.getMessage());
            return null;
        }
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