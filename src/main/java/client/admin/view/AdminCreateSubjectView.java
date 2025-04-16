package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class AdminCreateSubjectView {
    private final ComboBox<String> academicLevelComboBox;
    private final TextField subjectNameTextField;
    private final TextField subjectIDTextField;
    private final TextArea descriptionTextArea;
    private final Button addSubjectWindowButton;

    public AdminCreateSubjectView(ComboBox<String> academicLevelComboBox, TextField subjectNameTextField, TextField subjectIDTextField, TextArea descriptionTextArea, Button addSubjectWindowButton) {
        this.academicLevelComboBox = academicLevelComboBox;
        this.subjectNameTextField = subjectNameTextField;
        this.subjectIDTextField = subjectIDTextField;
        this.descriptionTextArea = descriptionTextArea;
        this.addSubjectWindowButton = addSubjectWindowButton;
    }

    public void initializeComboBoxes() {
        academicLevelComboBox.getItems().addAll("High School", "College");
    }

    public void setAddSubjectWindowButton(EventHandler<ActionEvent> handler) {
        if (addSubjectWindowButton != null) {
            addSubjectWindowButton.setOnAction(handler);
        }
    }
    public void setupButtonHoverEffects() {
        addSubjectWindowButton.setOnMouseEntered(this::handleButtonHover);
        addSubjectWindowButton.setOnMouseExited(this::handleButtonExit);
    }
    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addSubjectWindowButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addSubjectWindowButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public String getSelectedAcademicLevel() {
        return academicLevelComboBox.getValue();
    }

    public String getSubjectID() {
        return subjectIDTextField.getText();
    }
    public String getSubjectName() {
        return subjectNameTextField.getText();
    }

    public String getSubjectDescrip() {
        return descriptionTextArea.getText();
    }

}