package client;

import client.admin.model.AdminMainMenuModel;
import client.landingpage.login.AdminLoginController;
import client.landingpage.login.AdminLoginModel;
import client.landingpage.login.AdminLoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;

public class AdminClient extends Application {
    private static AdminService adminProcessors;
    private Stage primaryStage;
    private static AuthService authService;
    private static AdminService adminService;

    private static String SERVER_IP = "localhost"; // Default IP, will be updated by dialog
    private static final int PORT = 1099;

    public static AuthService getAuthService() {
        return authService;
    }

    public static AdminService getAdminService() {
        return adminService;
    }

    public static void main(String[] args) {
        System.out.println("");
        System.out.println(" █   █ ██▀ █   ▄▀▀ ▄▀▄ █▄ ▄█ ██▀   ▀█▀ ▄▀▄   █   ██▀ ▄▀▄ █▀▄ █▄ █ █ █▀ ▀▄▀\n" +
                " ▀▄▀▄▀ █▄▄ █▄▄ ▀▄▄ ▀▄▀ █ ▀ █ █▄▄    █  ▀▄▀   █▄▄ █▄▄ █▀█ █▀▄ █ ▀█ █ █▀  █ \n");

        System.out.println("=====================================================");
        System.out.println("[Admin Client] Starting client at " + new Date());
        System.out.println("=====================================================");

        // Show IP selection dialog before launching JavaFX
        SERVER_IP = showIPSelectionDialog();
        if (SERVER_IP == null) {
            System.out.println("[Admin Client] IP selection cancelled. Exiting.");
            System.exit(1);
        }

        launch(args);
    }

    public static AdminService getAdminProcessors() {
        return adminProcessors;
    }

    private static String showIPSelectionDialog() {
        JFrame frame = new JFrame("Select Server IP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Predefined IPs
        String[] predefinedIPs = {"localhost", "192.168.1.100", "192.168.191.115"};
        JComboBox<String> ipComboBox = new JComboBox<>(predefinedIPs);
        ipComboBox.setSelectedItem("localhost");
        ipComboBox.setPreferredSize(new Dimension(200, 25));

        JTextField customIPField = new JTextField();
        customIPField.setPreferredSize(new Dimension(200, 25));

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setPreferredSize(new Dimension(100, 30));

        // Disable dropdown when typing in text field
        customIPField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = customIPField.getText().trim();
                ipComboBox.setEnabled(text.isEmpty());
            }
        });

        // Select IP Label and ComboBox
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select IP:"), gbc);

        gbc.gridx = 1;
        panel.add(ipComboBox, gbc);

        // Enter Custom IP Label and TextField
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Enter IP:"), gbc);

        gbc.gridx = 1;
        panel.add(customIPField, gbc);

        // Confirm Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(confirmButton, gbc);

        final String[] selectedIP = {null};

        confirmButton.addActionListener(e -> {
            String customIP = customIPField.getText().trim();
            if (!customIP.isEmpty()) {
                selectedIP[0] = customIP;
            } else {
                selectedIP[0] = (String) ipComboBox.getSelectedItem();
            }
            frame.dispose();
        });

        frame.add(panel);
        frame.setVisible(true);

        // Wait for dialog to close
        try {
            while (frame.isVisible()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }

        return selectedIP[0];
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_IP, PORT);

            // Initialize all required services
            authService = (AuthService) registry.lookup("authentication");
            adminService = (AdminService) registry.lookup("admin_services");

            if (authService == null || adminService == null) {
                throw new Exception("One or more services are null after lookup.");
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

            // Get the controller
            AdminLoginView adminLoginView = loader.getController();
            if (adminLoginView == null) {
                System.err.println("[ERROR] AdminLoginView is NULL after FXML load!");
            } else {
                // Instantiate model and link with controller
                AdminLoginModel adminLoginModel = new AdminLoginModel(authService);
                new AdminLoginController(adminLoginView, adminLoginModel, authService, adminService);

                // Create an instance of AdminMainMenuModel
                AdminMainMenuModel adminMainMenuModel = new AdminMainMenuModel();
            }

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Learnify - Admin Portal");

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

            // Handle window close event
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Close request received. Terminating the application...");
                terminateApplication();
            });

            // Show the login page
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("[ERROR] Could not load admin_login_page.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error in loadAdminLoginPageUI(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void terminateApplication() {
        Platform.exit();
    }

    public static String getServerIP() {
        return SERVER_IP;
    }
}