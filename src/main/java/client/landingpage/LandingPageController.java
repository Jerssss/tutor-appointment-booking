package client.landingpage;


import client.StudentTutorClient;
import client.landingpage.login.LoginController;
import client.landingpage.login.LoginModel;
import client.landingpage.login.LoginView;
import client.landingpage.signup.SignUpController;
import client.landingpage.signup.SignUpModel;
import client.landingpage.signup.SignUpView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class LandingPageController {

    public LandingPageController(LandingPageView view) {
        if (view == null) {
            System.err.println("[CLIENT] LandingPageView is NULL! Button handlers will not be assigned.");
            return;
        }

        view.setActionSignInButton(this::handleSignIn);
        view.setActionSignUpButton(this::handleSignUp);
    }

    private void handleSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/login_page.fxml"));
            Parent root = loader.load();

            LoginView loginView = loader.getController();
            if (loginView == null) {
                System.err.println("[CLIENT] LoginView is NULL after loading FXML!");
                return;
            }

          //  new LoginController(loginView, new LoginModel(StudentTutorClient.getAuthService())); // Removed extra argument

            switchScene(event, root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showErrorDialog("Error loading the login page. Please try again.");
        }
    }

    private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/sign_up_page.fxml"));
            Parent root = loader.load();

            SignUpView signUpView = loader.getController();
            if (signUpView == null) {
                System.err.println("[CLIENT] SignUpView is NULL after loading FXML!");
                return;
            }

           // new SignUpController(signUpView, new SignUpModel());

            switchScene(event, root);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            showErrorDialog("Error loading the sign-up page. Please try again.");
        }
    }


    private void switchScene(ActionEvent event, Parent root) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    private void showErrorDialog(String message) {
        Platform.runLater(() -> JOptionPane.showMessageDialog(null, message, "Connection Error", JOptionPane.ERROR_MESSAGE));
    }

}
