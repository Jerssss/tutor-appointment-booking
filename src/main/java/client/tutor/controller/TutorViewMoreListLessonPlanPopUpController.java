package client.tutor.controller;

import client.tutor.model.TutorViewMoreLessonPlanPopUpModel;
import client.tutor.view.TutorViewMoreLessonPlanPopUp;

public class TutorViewMoreListLessonPlanPopUpController {
    private final TutorViewMoreLessonPlanPopUp view;
    private final TutorViewMoreLessonPlanPopUpModel model;

    public TutorViewMoreListLessonPlanPopUpController(TutorViewMoreLessonPlanPopUp view) {
        this.view = view;
        this.model = new TutorViewMoreLessonPlanPopUpModel();
    }
}