package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class AdminModifySessionPopUpView {
    private final ComboBox<String> sessionTypeComboBox;
    private final ComboBox<String> sessionModeComboBox;
    private final Button modifySessionButton;

    public AdminModifySessionPopUpView(ComboBox<String> sessionTypeComboBox, ComboBox<String> sessionModeComboBox, Button modifySessionButton) {
        this.sessionTypeComboBox = sessionTypeComboBox;
        this.sessionModeComboBox = sessionModeComboBox;
        this.modifySessionButton = modifySessionButton;
    }

    public void initializeComboBoxes() {
        sessionTypeComboBox.getItems().addAll("Individual", "Group");
        sessionModeComboBox.getItems().addAll("Online", "Face-To-Face");
    }

    public void setModifySessionButton(EventHandler<ActionEvent> handler) {
        if (modifySessionButton != null) {
            modifySessionButton.setOnAction(handler);
        }
    }
    public void setupButtonHoverEffects() {
        modifySessionButton.setOnMouseEntered(this::handleButtonHover);
        modifySessionButton.setOnMouseExited(this::handleButtonExit);
    }
    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifySessionButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifySessionButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public String getSelectedSessionType() {
        return sessionTypeComboBox.getValue();
    }
    public String getSelectedSessionMode() {
        return sessionModeComboBox.getValue();
    }

}