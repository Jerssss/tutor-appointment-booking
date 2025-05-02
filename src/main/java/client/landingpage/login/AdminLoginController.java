package client.landingpage.login;

import client.AdminClient;
import client.admin.controller.AdminMainMenuController;
import client.admin.model.AdminMainMenuModel;
import client.admin.view.AdminMainMenuView;
import javafx.event.ActionEvent;
import javax.swing.JOptionPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.classes.User;
import shared.exceptions.AccountDoesNotExist;
import shared.exceptions.AlreadyLoggedInException;
import shared.interfaces.AdminService;
import shared.interfaces.AuthService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;


public class AdminLoginController {
        private final AdminLoginView adminLoginView;
        private final AdminLoginModel adminLoginModel;
        private final AuthService authService;
        private final AdminService adminService; // Add this field

        // Update constructor to include AdminService
        public AdminLoginController(AdminLoginView adminLoginView,
                                    AdminLoginModel adminLoginModel,
                                    AuthService authService,
                                    AdminService adminService) {
            this.adminLoginView = adminLoginView;
            this.adminLoginModel = adminLoginModel;
            this.authService = authService;
            this.adminService = adminService; // Initialize the adminService

            this.adminLoginView.setLogInPageLogInButton(this::handleSignIn);
        }

        private void handleSignIn(ActionEvent event) {
            String userID = adminLoginView.getIdField().getText().trim();
            String password = adminLoginView.getPassField().getText().trim();

            if (userID.isEmpty() || password.isEmpty()) {
                adminLoginView.setPromptLabel("Please complete all fields.");
                adminLoginView.setPromptLabelVisible(true);
                return;
            }

            try {
                User user = AdminClient.getAuthService().login(userID, password);
                if (user != null && "Admin".equalsIgnoreCase(user.getRole())) {
                    System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Login successful for user: " + user.getUserID());
                    adminLoginView.setPromptLabel("Login successful!");
                    adminLoginView.setPromptLabelVisible(true);

                    // Pass the username to the redirect method
                    redirectToAdminMainMenu(event, user.getFirstName() + " " + user.getLastName());

                } else {
                    adminLoginView.setPromptLabel("Invalid credentials. Please try again.");
                }
            } catch (AccountDoesNotExist e) {
                adminLoginView.setPromptLabel("Invalid email or password. Please try again.");
                adminLoginView.setPromptLabelVisible(true);
            } catch (AlreadyLoggedInException e) {
                adminLoginView.setPromptLabel("Account was logged in elsewhere. You are now logged in.");
                adminLoginView.setPromptLabelVisible(true);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

    private void redirectToAdminMainMenu(ActionEvent event, String loggedInUserName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/admin/admin_menu_page.fxml"));
            Parent root = fxmlLoader.load();
            AdminMainMenuView adminMainMenuView = fxmlLoader.getController();
            new AdminMainMenuController(adminMainMenuView, new AdminMainMenuModel(), loggedInUserName);

            changeScene(event, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void changeScene(ActionEvent event, Parent root) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        }
}