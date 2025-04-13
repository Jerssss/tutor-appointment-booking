package client;

import client.landingpage.LandingPageController;
import client.landingpage.LandingPageView;
import client.tutor.controller.TutorCreateLessonPlanController;
import client.tutor.model.TutorCreateLessonPlanModel;
import client.tutor.view.TutorCreateLessonPlanPopUp; // Updated import
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shared.interfaces.TutorService;

import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class StudentTutorClient extends Application {
    private Stage primaryStage; // Declare primaryStage

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Initialize primaryStage
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            TutorService tutorService = (TutorService) registry.lookup("tutor_services");

            TutorCreateLessonPlanModel model = new TutorCreateLessonPlanModel(tutorService);
            TutorCreateLessonPlanController controller = new TutorCreateLessonPlanController(model);
            TutorCreateLessonPlanPopUp popUpView = new TutorCreateLessonPlanPopUp(); // Renamed

            controller.start(); // Test console flow
            loadLandingPageUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLandingPageUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/resource/fxml/common/landing_page.fxml"));
            Parent root = loader.load();

            // Get the controller
            LandingPageView landingPageView = loader.getController();
            if (landingPageView == null) {
                System.err.println("[ERROR] LandingPageView is NULL after FXML load!");
            } else {
                System.out.println("[DEBUG] LandingPageView controller loaded successfully."); // Debug log
                new LandingPageController(landingPageView); // Link the controller to the view
            }

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);

            primaryStage.setOnCloseRequest(event -> {
                System.out.println("[INFO] Close request received. Terminating the application...");
                terminateApplication();
            });
            // Show the landing page
            primaryStage.show();

            System.out.println("[Client] WELCOME TO LENDIFY");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Could not load landing_page.fxml: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] Unexpected error in loadLandingPageUI(): " + e.getMessage());
        }
    }

    private void terminateApplication() {
        Platform.exit(); // Properly exit the application
    }
}