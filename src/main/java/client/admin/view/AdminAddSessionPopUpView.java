package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

public class AdminAddSessionPopUpView {
    private final ComboBox<String> tutorComboBox;
    private final ComboBox<String> subjectComboBox;
    private final ComboBox<String> sessionModeComboBox;
    private final ComboBox<String> sessionTypeComboBox;
    private final ComboBox<String> startTimeComboBox;
    private final ComboBox<String> durationComboBox;
    private final DatePicker datePicker;
    private final Button addSessionWindowButton;

    private final TextField sessionPriceTextField;
    private final TextField maxStudentsTextField;

    public AdminAddSessionPopUpView(
            ComboBox<String> tutorComboBox,
            ComboBox<String> subjectComboBox,
            ComboBox<String> sessionModeComboBox,
            ComboBox<String> sessionTypeComboBox,
            ComboBox<String> startTimeComboBox,
            ComboBox<String> durationComboBox,
            DatePicker datePicker,
            Button addSessionWindowButton, TextField sessionPriceTextField, TextField maxStudentsTextField) {

        this.tutorComboBox = tutorComboBox;
        this.subjectComboBox = subjectComboBox;
        this.sessionModeComboBox = sessionModeComboBox;
        this.sessionTypeComboBox = sessionTypeComboBox;
        this.startTimeComboBox = startTimeComboBox;
        this.durationComboBox = durationComboBox;
        this.datePicker = datePicker;
        this.addSessionWindowButton = addSessionWindowButton;
        this.sessionPriceTextField = sessionPriceTextField;
        this.maxStudentsTextField = maxStudentsTextField;
    }

    public void initializeComboBoxes(List<String> tutors, List<String> subjects) {
        tutorComboBox.getItems().addAll(tutors);
        subjectComboBox.getItems().addAll(subjects);
        sessionModeComboBox.getItems().addAll("Online", "Face-to-Face");
        sessionTypeComboBox.getItems().addAll("Individual", "Group");
    }

    public void setTutorSelectionHandler(EventHandler<ActionEvent> handler) {
        tutorComboBox.setOnAction(handler);
    }

    public void setStartTimeSelectionHandler(EventHandler<ActionEvent> handler) {
        startTimeComboBox.setOnAction(handler);
    }

    public void setDurationSelectionHandler(EventHandler<ActionEvent> handler) {
        durationComboBox.setOnAction(handler);
    }

    public void setSubjectSelectionHandler(EventHandler<ActionEvent> handler) {
        subjectComboBox.setOnAction(handler);
    }

    public void setModeSelectionHandler(EventHandler<ActionEvent> handler) {
        sessionModeComboBox.setOnAction(handler);
    }

    public void setTypeSelectionHandler(EventHandler<ActionEvent> handler) {
        sessionTypeComboBox.setOnAction(handler);
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

    public String getSelectedTutor() {
        return tutorComboBox.getValue();
    }

    public String getSelectedStartTime() {
        return startTimeComboBox.getValue();
    }

    public String getSelectedDuration() {
        return durationComboBox.getValue();
    }

    public String getSelectedSubject() {
        return subjectComboBox.getValue();
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
}