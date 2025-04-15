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
import shared.interfaces.AuthService;
import shared.interfaces.StudentService;
import shared.interfaces.TutorService;


import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;



public class StudentTutorClient extends Application {
    private Stage primaryStage;
    private static AuthService authService;
    private static StudentService studentService;
    private static TutorService tutorService;

    public static AuthService getAuthService() {
        return authService;
    }

    public static StudentService getStudentService() {
        return studentService;
    }
    public static TutorService getTutorService() {
        return tutorService;
    }

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Initialize all required services
            authService = (AuthService) registry.lookup("authentication");
            studentService = (StudentService) registry.lookup("student_service");
            tutorService = (TutorService) registry.lookup("tutor_services");

            loadLandingPageUI();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }


    private void loadLandingPageUI() {
        try {
            File fxmlFile = new File("src/main/resource/fxml/common/landing_page.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
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
