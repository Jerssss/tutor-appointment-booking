package client;

import client.tutor.controller.TutorCreateLessonPlanController;
import client.tutor.model.TutorCreateLessonPlanModel;
import client.tutor.view.TutorCreateLessonPlanPopUp; // Updated import
import shared.interfaces.TutorService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TutorClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            TutorService tutorService = (TutorService) registry.lookup("tutor_services");

            TutorCreateLessonPlanModel model = new TutorCreateLessonPlanModel(tutorService);
            TutorCreateLessonPlanController controller = new TutorCreateLessonPlanController(model);
            TutorCreateLessonPlanPopUp popUpView = new TutorCreateLessonPlanPopUp(); // Renamed

            controller.start(); // Test console flow
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}