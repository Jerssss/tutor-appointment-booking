package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class AdminModifySubjectPopUpView {
    private final ComboBox<String> subjectVisibilityComboBox;
    private final ComboBox<String> academicLevelComboBox;
    private final Button modifySubjectButton;

    public AdminModifySubjectPopUpView(ComboBox<String> subjectVisibilityComboBox, ComboBox<String> academicLevelComboBox, Button modifySubjectButton) {
        this.subjectVisibilityComboBox = subjectVisibilityComboBox;
        this.academicLevelComboBox = academicLevelComboBox;
        this.modifySubjectButton = modifySubjectButton;
    }

    public void initializeComboBoxes() {
        subjectVisibilityComboBox.getItems().addAll("Available", "Archived");
        academicLevelComboBox.getItems().addAll("High School", "College");
    }

    public void setModifySubjectButton(EventHandler<ActionEvent> handler) {
        if (modifySubjectButton != null) {
            modifySubjectButton.setOnAction(handler);
        }
    }
    public void setupButtonHoverEffects() {
        modifySubjectButton.setOnMouseEntered(this::handleButtonHover);
        modifySubjectButton.setOnMouseExited(this::handleButtonExit);
    }
    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifySubjectButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifySubjectButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public String getSelectedSubjectVisibility() {
        return subjectVisibilityComboBox.getValue();
    }

    public String getSelectedAcademicLevel() {
        return academicLevelComboBox.getValue();
    }

}