package client.landingpage;


import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.Date;


public class LandingPageView {


    @FXML
    private Button landingPageLogInButton; // Button for logging in
    @FXML
    private Button landingPageSignUpButton; // Button for signing up

    public void initialize() {
        if (landingPageLogInButton == null) {
        }
        if (landingPageSignUpButton == null) {
        }
    }

    public void setActionSignInButton(EventHandler<ActionEvent> event) {
        if (landingPageLogInButton != null) {
            landingPageLogInButton.setOnAction(event);
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] landingPageLogInButton");
        }
    }

    //Applies a hover effect to the "Log In" button by scaling it down.
    @FXML
    public void logInButtonHovered() {
        if (landingPageLogInButton != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageLogInButton);
            st.setToX(0.9);
            st.setToY(0.9);
            st.setCycleCount(1);
            st.setAutoReverse(false);
            st.play();
        } else {
            System.err.println("[ERROR] landingPageLogInButton is NULL!");
        }
    }

    //Resets the "Log In" button to its original size when the hover ends.
    @FXML
    public void logInButtonExited() {
        if (landingPageLogInButton != null) {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), landingPageLogInButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setCycleCount(1);
            st.setAutoReverse(false);
            st.play();
        } else {
            System.err.println("[ERROR] landingPageLogInButton is NULL!");
        }
    }
}
