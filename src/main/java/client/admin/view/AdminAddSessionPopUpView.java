package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

public class AdminAddSessionPopUpView {
    private final ComboBox<String> tutorIDComboBox;
    private final ComboBox<String> subjectNameComboBox;
    private final ComboBox<String> sessionModeComboBox;
    private final ComboBox<String> sessionTypeComboBox;
    private final ComboBox<String> startTimeComboBox;
    private final ComboBox<String> durationComboBox;
    private final ComboBox<String> tutorNameComboBox;
    private final ComboBox<String> subjectIdComboBox;
    private final DatePicker datePicker;
    private final Button addSessionWindowButton;

    private final TextField sessionPriceTextField;
    private final TextField maxStudentsTextField;

    public AdminAddSessionPopUpView(
            ComboBox<String> tutorIDComboBox,
            ComboBox<String> subjectNameComboBox,
            ComboBox<String> sessionModeComboBox,
            ComboBox<String> sessionTypeComboBox,
            ComboBox<String> startTimeComboBox,
            ComboBox<String> durationComboBox, ComboBox<String> tutorNameComboBox, ComboBox<String> subjectIdComboBox,
            DatePicker datePicker,
            Button addSessionWindowButton, TextField sessionPriceTextField, TextField maxStudentsTextField) {

        this.tutorIDComboBox = tutorIDComboBox;
        this.subjectNameComboBox = subjectNameComboBox;
        this.sessionModeComboBox = sessionModeComboBox;
        this.sessionTypeComboBox = sessionTypeComboBox;
        this.startTimeComboBox = startTimeComboBox;
        this.durationComboBox = durationComboBox;
        this.tutorNameComboBox = tutorNameComboBox;
        this.subjectIdComboBox = subjectIdComboBox;
        this.datePicker = datePicker;
        this.addSessionWindowButton = addSessionWindowButton;
        this.sessionPriceTextField = sessionPriceTextField;
        this.maxStudentsTextField = maxStudentsTextField;
    }

    public void initializeComboBoxes(List<String> tutorNames, List<String> tutorIDs) {
        tutorNameComboBox.getItems().addAll(tutorNames);
        tutorIDComboBox.getItems().addAll(tutorIDs);
        sessionModeComboBox.getItems().addAll("Online", "Face-to-Face");
        sessionTypeComboBox.getItems().addAll("Individual", "Group");
    }

    public void setTutorSelectionHandler(EventHandler<ActionEvent> handler) {
        tutorIDComboBox.setOnAction(handler);
    }

    public void setStartTimeSelectionHandler(EventHandler<ActionEvent> handler) {
        startTimeComboBox.setOnAction(handler);
    }

    public void setDateSelectionHandler(EventHandler<ActionEvent> handler) {
        datePicker.setOnAction(handler);
    }

    public void setAddSessionButtonAction(EventHandler<ActionEvent> handler) {
        if (addSessionWindowButton != null) {
            addSessionWindowButton.setOnAction(handler);
        }
    }

    public void setupButtonHoverEffects() {
        addSessionWindowButton.setOnMouseEntered(this::handleButtonHover);
        addSessionWindowButton.setOnMouseExited(this::handleButtonExit);
    }

    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addSessionWindowButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addSessionWindowButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void showErrorBelowTextField(TextField field, String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
        VBox container = (VBox) field.getParent();
        if (container.getChildren().size() < 3) {
            container.getChildren().add(errorLabel);
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

    public void showErrorBelowDatePicker(DatePicker picker, String message) {
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 10;");
        VBox container = (VBox) picker.getParent();
        if (container.getChildren().size() < 3) {
            container.getChildren().add(errorLabel);
        }
    }

    public void clearAllErrorMessages() {
        List<Control> controls = List.of(
                tutorIDComboBox, tutorNameComboBox, subjectIdComboBox, subjectNameComboBox, sessionModeComboBox,
                sessionTypeComboBox, startTimeComboBox, durationComboBox,
                datePicker, maxStudentsTextField, sessionPriceTextField
        );

        for (Control control : controls) {
            VBox container = (VBox) control.getParent();
            if (container.getChildren().size() == 3) {
                container.getChildren().remove(2);
            }
        }
    }

    public String getSelectedTutorName() {
        return tutorNameComboBox.getValue();
    }
    public String getSelectedTutorID() {
        return tutorIDComboBox.getValue();

    }

    public String getSelectedStartTime() {
        return startTimeComboBox.getValue();
    }

    public String getSelectedDuration() {
        return durationComboBox.getValue();
    }

    public String getSelectedSubjectName() {
        return subjectNameComboBox.getValue();
    }
    public String getSelectedSubjectID() {
        return subjectIdComboBox.getValue();
    }

    public String getSelectedMode() {
        return sessionModeComboBox.getValue();
    }

    public String getSelectedType() {
        return sessionTypeComboBox.getValue();
    }

    public LocalDate getSelectedDate() {
        return datePicker.getValue();
    }

    public String getPrice() {
        return sessionPriceTextField.getText();
    }

    public String getMaxStudents(){
        return maxStudentsTextField.getText();
    }

    public void updateStartTimes(List<String> times) {
        startTimeComboBox.getItems().clear();
        startTimeComboBox.getItems().addAll(times);
    }

    public void updateDurations(List<String> durations) {
        durationComboBox.getItems().clear();
        durationComboBox.getItems().addAll(durations);
    }

    public void updateSubjectName(List<String> subjects){
        subjectNameComboBox.getItems().clear();
        subjectNameComboBox.getItems().addAll(subjects);
    }

    public void updateSubjectID(List<String> subjects){
        subjectIdComboBox.getItems().clear();
        subjectIdComboBox.getItems().addAll(subjects);
    }
}