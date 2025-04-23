package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.List;


public class AdminDeleteSessionPopUpView {
    private final Button confirmButton;
    private final Button cancelButton;

    public AdminDeleteSessionPopUpView(Button cancelButton, Button confirmButton) {
        this.cancelButton = cancelButton;
        this.confirmButton = confirmButton;
    }

    public void setCancelButton(EventHandler<ActionEvent> handler) {
        if (cancelButton != null) {
            cancelButton.setOnAction(handler);
        }
    }
    public void setConfirmButton(EventHandler<ActionEvent> handler) {
        if (confirmButton != null) {
            confirmButton.setOnAction(handler);
        }
    }
    public void setupButtonHoverEffects() {
        cancelButton.setOnMouseEntered(this::handleButtonHover);
        cancelButton.setOnMouseExited(this::handleButtonExit);
        confirmButton.setOnMouseEntered(this::handleConfirmButtonHover);
        confirmButton.setOnMouseExited(this::handleConfirmButtonExit);
    }
    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), cancelButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), cancelButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void handleConfirmButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), confirmButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleConfirmButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), confirmButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

}