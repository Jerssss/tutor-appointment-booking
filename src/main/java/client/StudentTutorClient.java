package client;

import client.landingpage.LandingPageController;
import client.landingpage.LandingPageView;
import client.studenttutor.StudentTutorIPPickerController;
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
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class StudentTutorClient extends Application {
    private Stage primaryStage;
    private static AuthService authService;
    private static StudentService studentService;
    private static TutorService tutorService;
    private static String SERVER_IP = "localhost"; // Default IP, updated by dialog
    private static final int PORT = 1099;
    private static Registry registry;

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
        System.out.println("");
        System.out.println(" █   █ ██▀ █   ▄▀▀ ▄▀▄ █▄ ▄█ ██▀   ▀█▀ ▄▀▄   █   ██▀ ▄▀▄ █▀▄ █▄ █ █ █▀ ▀▄▀\n" +
                           " ▀▄▀▄▀ █▄▄ █▄▄ ▀▄▄ ▀▄▀ █ ▀ █ █▄▄    █  ▀▄▀   █▄▄ █▄▄ █▀█ █▀▄ █ ▀█ █ █▀  █ \n");

        System.out.println("=====================================================");
        System.out.println("[Client] Starting client at " + new Date());
        System.out.println("=====================================================");

        launch(args);
    }

    private String showIPSelectionDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ippicker/student_tutor_ip_picker.fxml"));
            Parent root = loader.load();
            StudentTutorIPPickerController controller = loader.getController();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Select Student & Tutor Client IP");
            dialogStage.setScene(new Scene(root, 350, 250));
            dialogStage.setResizable(false);

            dialogStage.setOnHidden(event -> {
                if (controller.getSelectedIP() == null) {
                    Platform.exit();
                }
            });

            dialogStage.showAndWait();
            return controller.getSelectedIP();

        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load IP Picker: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            String selectedIP = showIPSelectionDialog();
            if (selectedIP == null) {
                System.out.println("[StudentTutor Client] IP selection cancelled. Exiting.");
                Platform.exit();
                return;
            }

            SERVER_IP = selectedIP;

            registry = LocateRegistry.getRegistry(SERVER_IP, PORT);

            // Initialize all required services
            authService = (AuthService) registry.lookup("authentication");
            studentService = (StudentService) registry.lookup("student_service");
            tutorService = (TutorService) registry.lookup("tutor_services");

            loadLandingPageUI();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void loadLandingPageUI() {
        try {
            File fxmlFile = new File("src/main/resources/fxml/common/landing_page.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            // Get the controller
            LandingPageView landingPageView = loader.getController();
            if (landingPageView == null) {
                System.err.println("[ERROR] LandingPageView is NULL after FXML load!");
            } else {
                new LandingPageController(landingPageView); // Link the controller to the view
            }

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Learnify - Student & Tutor Portal");

            // Load application icon
            try {
                URL iconUrl = StudentTutorClient.class.getResource("/images/client/app_icon.png");
                if (iconUrl == null) {
                    throw new RuntimeException("Icon file not found!");
                }
                primaryStage.getIcons().add(new Image(iconUrl.openStream()));
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to load icon: " + e.getMessage());
                e.printStackTrace();
            }

            primaryStage.setOnCloseRequest(event -> {
                System.out.println("[INFO] Close request received. Terminating the application...");
                terminateApplication();
            });

            // Show the landing page
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("[ERROR] Could not load landing_page.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error in loadLandingPageUI(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void terminateApplication() {
        System.out.println("[CLIENT | "+ new Date()+ "] Cleaning up RMI resources before exit...");

        // Clean up RMI references
        authService = null;
        studentService = null;
        tutorService = null;

        // Unbind registry
        if (registry != null) {
            try {
                // clean up local references
                System.out.println("[CLIENT | "+ new Date()+ "] Cleaning up registry reference");
            } catch (Exception e) {
                System.err.println("[ERROR] Error cleaning up registry: " + e.getMessage());
            }
        }

        // Force garbage collection to help clean up RMI references
        System.gc();

        System.out.println("[CLIENT | "+ new Date()+ "] Terminating the application...");
        Platform.exit();
        System.exit(0); // Ensure complete shutdown
    }

    public static String getServerIP() {
        return SERVER_IP;
    }
}