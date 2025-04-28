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

    public List<TutorSession> fetchSessions(String tutorID) {
        try {
            System.out.println("Fetching sessions for tutor ID: " + tutorID);
            List<TutorSession> sessions = tutorService.viewSessionList(tutorID);
            System.out.println("Sessions fetched: " + (sessions != null ? sessions.size() : 0));
            return sessions;
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to fetch sessions: " + e.getMessage());
            return null;
        }
    }

    public List<TutorSession> getSessionList(String tutorID) {
        return fetchSessions(tutorID);
    }
}