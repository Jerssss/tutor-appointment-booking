package client.tutor.model;

import client.StudentTutorClient;
import shared.interfaces.TutorService;

public class TutorMainMenuModel {
    private TutorService tutorService;

    public TutorMainMenuModel(TutorService tutorService) {
        this.tutorService = StudentTutorClient.getTutorService();
    }
}
