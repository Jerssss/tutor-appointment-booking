package client.tutor.view;

import client.tutor.controller.TutorCreateLessonPlanController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.LessonPlan;
import shared.classes.SessionManager;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class TutorCreateLessonPlanPopUp {
    @FXML private ComboBox<String> academicLevelComboBox;
    @FXML private ComboBox<String> subjectComboBox;
    @FXML private TextField objectivesTextField;
    @FXML private TextField topicsCoveredTextField;
    @FXML private Button addLessonPlanButton;
    private TutorCreateLessonPlanController controller;

    public void initialize() {
        initializeController();
        academicLevelComboBox.getItems().addAll("High School", "College");

        // Get the logged-in tutor ID from the SessionManager
        String loggedInTutorID = SessionManager.getCurrentUserId();
        System.out.println("[CLIENT | "+ new Date()+ "] Logged in Tutor ID: " + loggedInTutorID);

        List<String> subjects = controller.fetchSubjectsByExpertise(loggedInTutorID);
        System.out.println("[CLIENT | "+ new Date()+ "] Fetched subjects: " + subjects);

        if (subjects != null && !subjects.isEmpty()) {
            subjectComboBox.getItems().addAll(subjects);
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No subjects found for the logged-in tutor.");
        }

        // Set up the action listener for the subjectComboBox
        subjectComboBox.setOnAction(event -> autofillFields());
        addLessonPlanButton.setOnAction(event -> {
            try {
                handleSave();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void initializeController() {
        System.out.println("[CLIENT | "+ new Date()+ "] Initializing TutorCreateLessonPlanController...");
        this.controller = new TutorCreateLessonPlanController();
        System.out.println("[CLIENT | "+ new Date()+ "] TutorCreateLessonPlanController successfully created.");
    }

    private void autofillFields() {
        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            // Fetch lesson plan details based on the selected subject
            LessonPlan lessonPlanDetails = controller.getLessonPlanDetails(selectedSubject);
            if (lessonPlanDetails != null) {
                objectivesTextField.setText(lessonPlanDetails.getObjectives());
                topicsCoveredTextField.setText(lessonPlanDetails.getTopicsCovered());
            }
        }
    }

    private void handleSave() throws RemoteException {
        String acadLvl = academicLevelComboBox.getValue();
        String subjectName = subjectComboBox.getValue(); // This is the subject name
        String objectives = objectivesTextField.getText();
        String topicsCovered = topicsCoveredTextField.getText();

        if (acadLvl == null || subjectName == null || objectives.isEmpty() || topicsCovered.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields must be filled in. Please check again.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Fetch the subject ID based on the selected subject name
        String subjectID = String.valueOf(controller .getSubjectIDByName(subjectName));
        if (subjectID == null) {
            JOptionPane.showMessageDialog(null, "Subject ID not found for the selected subject.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Now pass the correct subjectID to the addNewLessonPlan method
        boolean success = controller.addNewLessonPlan(acadLvl, subjectID, subjectName, objectives, topicsCovered);
        if (!success) {
            JOptionPane.showMessageDialog(null, "Failed to add lesson plan.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Lesson plan added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addLessonPlanButton.getScene().getWindow();
        stage.close();
    }

    public void addLessonPlanButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addLessonPlanButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}