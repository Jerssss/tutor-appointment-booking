package client.landingpage.login;


import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import shared.classes.SessionManager;

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

        if (logInPageLogInButton == null) {
            System.err.println("logInPageLogInButton");
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
        if (logInPageLogInButton != null) {
            logInPageLogInButton.setOnAction(event);
        } else {
            System.out.println("logInPageLogInButton");
        }
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