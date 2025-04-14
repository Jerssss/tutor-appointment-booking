package client.landingpage.login;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class AdminLoginView {
    @FXML
    private Button logInPageLogInButton; // Button for logging in
    @FXML
    private TextField idField; // Text field for entering user ID
    @FXML
    private PasswordField passField; // Password field for entering password
    @FXML
    private Label promptLabel; // Label for displaying prompts or error messages


    public Button getLogInPageLogInButton() {
        return logInPageLogInButton;
    }

    public void setLogInPageLogInButton(Button logInPageLogInButton) {
        this.logInPageLogInButton = logInPageLogInButton;
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


    public Label getPromptLabel() {
        return promptLabel;
    }


    public void setPromptLabel(Label promptLabel) {
        this.promptLabel = promptLabel;
    }


    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        if (logInPageLogInButton != null) {
            logInPageLogInButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageLogInButton is NULL! Check FXML.");
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
