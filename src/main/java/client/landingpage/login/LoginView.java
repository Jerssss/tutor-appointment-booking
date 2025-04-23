package client.landingpage.login;


import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import static java.awt.SystemColor.text;


public class LoginView {
    @FXML
    private Button logInPageLogInButton; // Button for logging in
    @FXML
    private Button logInPageSignUpButton; // Button for signing up
    @FXML
    private TextField idField; // Text field for entering user ID
    @FXML
    private PasswordField passField; // Password field for entering password
    @FXML
    private ComboBox<String> userTypeBox; // Combo box for selecting user type
    @FXML
    private Label promptLabel; // Label for displaying prompts or error messages



    public void initialize() {
        System.out.println("[DEBUG] Initializing Login View...");

        if (logInPageLogInButton == null) {
            System.err.println("logInPageLogInButton");
        }
        if (logInPageSignUpButton == null) {
            System.err.println("logInPageSignUpButton");
        }
    }


    public Button getLogInPageLogInButton() {
        return logInPageLogInButton;
    }


    public void setLogInPageLogInButton(Button logInPageLogInButton) {
        this.logInPageLogInButton = logInPageLogInButton;
    }


    public Button getLogInPageSignUpButton() {
        return logInPageSignUpButton;
    }


    public void setLogInPageSignUpButton(Button logInPageSignUpButton) {
        this.logInPageSignUpButton = logInPageSignUpButton;
    }


    public TextField getIdField() {
        return idField;
    }


    public void setIdField(TextField idField) {
        this.idField = idField;
    }


    public PasswordField getPassField() {
        return passField;
    }


    public void setPassField(PasswordField passField) {
        this.passField = passField;
    }


    public ComboBox<String> getUserTypeBox() {
        return userTypeBox;
    }


    public void setUserTypeBox(ComboBox<String> userTypeBox) {
        this.userTypeBox = userTypeBox;
    }


    public Label getPromptLabel() {
        return promptLabel;
    }


    public void setPromptLabel(String text) {
        promptLabel.setText(text);
    }

    public void setPromptLabelVisible(boolean visible) {
        promptLabel.setVisible(visible);
    }

    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        System.out.println("[DEBUG] Setting action for login button");
        if (logInPageLogInButton != null) {
            System.out.println("[DEBUG] Button is not null, setting handler");
            logInPageLogInButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageLogInButton is NULL! Check FXML.");
        }
    }


    public void setActionSignUpButton(EventHandler<ActionEvent> event) {
        if (logInPageSignUpButton != null) {
            logInPageSignUpButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageSignUpButton is NULL! Check FXML.");
        }
    }


    public void signUpButtonExited() {
        animateButton(logInPageSignUpButton, 1.0);
    }
    public void signUpButtonHovered() {
        animateButton(logInPageSignUpButton, 0.9);
    }
    public void logInButtonExited() {
        animateButton(logInPageLogInButton, 1.0);
    }
    public void logInButtonHovered() {
        animateButton(logInPageLogInButton, 0.9);
    }




    private void animateButton(Button button, double scale) {
        if (button != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(scale);
            st.setToY(scale);
            st.setCycleCount(1);
            st.setAutoReverse(false);
            st.play();
        }
    }
}