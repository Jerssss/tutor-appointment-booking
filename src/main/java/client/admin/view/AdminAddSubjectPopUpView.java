package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;


public class AdminAddSubjectPopUpView {
    private final ComboBox<String> academicLevelComboBox;
    private final TextField subjectNameTextField;
    private final TextField subjectIDTextField;
    private final TextArea descriptionTextArea;
    private final Button addSubjectWindowButton;

    public AdminAddSubjectPopUpView(ComboBox<String> academicLevelComboBox, TextField subjectNameTextField, TextField subjectIDTextField, TextArea descriptionTextArea, Button addSubjectWindowButton) {
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

    public void showErrorBelowTextField(TextField field, String message) {
        if (field.getParent() instanceof VBox container) {
            Label errorLabel = new Label(message);
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
            if (container.getChildren().size() < 3) {
                container.getChildren().add(errorLabel);
            }
        } else {
            System.out.println("[DEBUG] TextField is not inside a VBox!");
        }
    }


    public void showErrorBelowComboBox(ComboBox<?> comboBox, String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
        VBox container = (VBox) comboBox.getParent();
        if (container.getChildren().size() < 3) {
            container.getChildren().add(errorLabel);
        }
    }

    public void clearAllErrorMessages() {
        List<Control> controls = List.of(
                academicLevelComboBox, subjectNameTextField, subjectIDTextField, descriptionTextArea
        );
        for (Control control : controls) {
            VBox container = (VBox) control.getParent();
            if (container.getChildren().size() == 3) {
                container.getChildren().remove(2);
            }
        }
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