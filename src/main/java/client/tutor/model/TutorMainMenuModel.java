package client.tutor.model;

import shared.interfaces.TutorService;

public class TutorMainMenuModel {
    private TutorService tutorService;

    public TutorMainMenuModel(TutorService tutorService) {
        this.tutorService = tutorService;
    }
}
