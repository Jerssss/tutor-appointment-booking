package client;

import client.landingpage.LandingPageController;
import client.landingpage.LandingPageView;
import client.landingpage.login.AdminLoginController;
import client.landingpage.login.AdminLoginModel;
import client.landingpage.login.AdminLoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class AdminClient extends Application {
    private Stage primaryStage; // Declare primaryStage
    private static AuthService authService; // Declare the authentication service
    private static AdminService adminService; // Declare the admin service

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("[Admin Client] Starting client at " + new Date());
        System.out.println("=====================================================");

        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Initialize primaryStage
        try {

            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            authService = (AuthService) registry.lookup("authentication");
            adminService = (AdminService) registry.lookup("admin_services");

            // Load the admin login page UI
            loadAdminLoginPageUI();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAdminLoginPageUI() {
        try {
            File fxmlFile = new File("src/main/resource/fxml/admin/admin_login_page.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            // Get the controller
            AdminLoginView adminLoginView = loader.getController();
            if (adminLoginView == null) {
                System.err.println("[ERROR] AdminLoginPageView is NULL after FXML load!");
            } else {
                System.out.println("[DEBUG] AdminLoginPageView controller loaded successfully."); // Debug log
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

            System.out.println("[Admin Client] WELCOME TO LENDIFY");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[ERROR] Could not load admin_login_page.fxml: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] Unexpected error in loadAdminLoginPageUI(): " + e.getMessage());
        }
    }

    private void terminateApplication() {
        Platform.exit(); // Properly exit the application
    }
}