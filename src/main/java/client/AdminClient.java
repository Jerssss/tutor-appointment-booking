package client;

import client.admin.model.AdminMainMenuModel;
import client.landingpage.login.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class AdminClient extends Application {
    private Stage primaryStage;
    private static AuthService authService;
    private static AdminService adminService;
    private static String serverIP = "localhost"; // Server IP will be set by the user
    private static final int PORT = 1099;

    public static AuthService getAuthService() {
        return authService;
    }

    public static AdminService getAdminService() {
        return adminService;
    }

    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("[Admin Client] Starting client at " + new Date());
        System.out.println("=====================================================");

        launch(args);
    }

    public static AdminService getAdminProcessors() {
        return adminService;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            Registry registry = LocateRegistry.getRegistry(serverIP, PORT);
            authService = (AuthService) registry.lookup("authentication");
            adminService = (AdminService) registry.lookup("admin_services");

            if (authService == null) {
                throw new Exception("AuthService is null after lookup.");
            }
            if (adminService == null) {
                throw new Exception("AdminService is null after lookup.");
            }

            loadAdminLoginPageUI();
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void loadAdminLoginPageUI() {
        try {
            File fxmlFile = new File("src/main/resources/fxml/admin/admin_login_page.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Parent root = loader.load();

            AdminLoginView adminLoginView = loader.getController();
            if (adminLoginView == null) {
                System.err.println("[ERROR] AdminLoginPageView is NULL after FXML load!");
            } else {
                System.out.println("[DEBUG] AdminLoginPageView controller loaded successfully."); // Debug log

                // Create an instance of AdminLoginModel
                AdminLoginModel adminLoginModel = new AdminLoginModel(authService);

                // Pass the view, model, and authService to the controller
                new AdminLoginController(adminLoginView, adminLoginModel, authService, adminService);

                // Create an instance of AdminMainMenuModel
                AdminMainMenuModel adminMainMenuModel = new AdminMainMenuModel(adminService);
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Admin Portal - Learnify");

            // Load application icon
            try {
                URL iconUrl = AdminClient.class.getResource("/images/client/app_icon.png");
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

            primaryStage.show();
            System.out.println("[Admin Client] WELCOME TO LEARNIFY ADMIN PORTAL");
        } catch (IOException e) {
            System.err.println("[ERROR] Could not load admin_login_page.fxml: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void terminateApplication() {
        Platform.exit();
        System.exit(0);
    }

    public static String getServerIP() {
        return serverIP;
    }


}