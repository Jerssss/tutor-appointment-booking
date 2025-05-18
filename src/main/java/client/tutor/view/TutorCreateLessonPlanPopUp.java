package client.tutor.view;

import client.tutor.controller.TutorCreateLessonPlanController;
import client.tutor.controller.TutorViewLessonPlanController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    @FXML private Label academicLevelLabel;
    @FXML private ComboBox<String> subjectComboBox;
    @FXML private TextField objectivesTextField;
    @FXML private TextField topicsCoveredTextField;
    @FXML private Button addLessonPlanButton;
    private TutorCreateLessonPlanController controller;
    private TutorViewLessonPlanController parentController;

    public void setParentController(TutorViewLessonPlanController parentController) {
        this.parentController = parentController;
    }

    public void initialize() {
        initializeController();

        String loggedInTutorID = SessionManager.getCurrentUserId();
        System.out.println("[CLIENT | " + new Date() + "] Logged in Tutor ID: " + loggedInTutorID);

        List<String> subjects = controller.fetchSubjectsByExpertise(loggedInTutorID);
        System.out.println("[CLIENT | " + new Date() + "] Fetched subjects: " + subjects);

        if (subjects != null && !subjects.isEmpty()) {
            subjectComboBox.getItems().addAll(subjects);
        } else {
            System.err.println("[CLIENT | " + new Date() + "] No subjects found for tutor ID: " + loggedInTutorID);
            JOptionPane.showMessageDialog(null,
                    "No subjects found for your expertise. Please ensure your expertise is set in the system.",
                    "No Subjects Available",
                    JOptionPane.WARNING_MESSAGE);
            // For debugging, add dummy subjects
            // subjectComboBox.getItems().addAll("Mathematics", "Physics");
        }

        subjectComboBox.setOnAction(event -> {
            try {
                updateAcademicLevel();
                autofillFields();
            } catch (RemoteException e) {
                System.err.println("[CLIENT ERROR] Error updating fields: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Error updating fields: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        addLessonPlanButton.setOnAction(event -> {
            try {
                handleSave();
            } catch (RemoteException e) {
                System.err.println("[CLIENT ERROR] Error saving lesson plan: " + e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "Error saving lesson plan: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void initializeController() {
        System.out.println("[CLIENT | " + new Date() + "] Initializing TutorCreateLessonPlanController...");
        this.controller = new TutorCreateLessonPlanController();
        System.out.println("[CLIENT | " + new Date() + "] TutorCreateLessonPlanController successfully created.");
    }

    private void updateAcademicLevel() throws RemoteException {
        if (academicLevelLabel == null) {
            System.err.println("[CLIENT ERROR] academicLevelLabel is null. Verify FXML fx:id='academicLevelLabel'.");
            JOptionPane.showMessageDialog(null,
                    "UI error: Academic Level Label not found. Please contact support.",
                    "UI Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            String academicLevel = controller.getAcademicLevelBySubjectName(selectedSubject);
            if (academicLevel != null && (academicLevel.equals("High School") || academicLevel.equals("College"))) {
                academicLevelLabel.setText(academicLevel);
            } else {
                academicLevelLabel.setText("Invalid academic level");
                System.err.println("[CLIENT ERROR] Invalid or null academic level for subject: " + selectedSubject);
            }
        } else {
            academicLevelLabel.setText("Select a subject");
        }
    }

    private void autofillFields() throws RemoteException {
        String selectedSubject = subjectComboBox.getValue();
        if (selectedSubject != null) {
            String subjectID = controller.getSubjectIDByName(selectedSubject);
            if (subjectID != null) {
                LessonPlan lessonPlanDetails = controller.getLessonPlanDetails(subjectID);
                if (lessonPlanDetails != null) {
                    objectivesTextField.setText(lessonPlanDetails.getObjectives());
                    topicsCoveredTextField.setText(lessonPlanDetails.getTopicsCovered());
                } else {
                    objectivesTextField.clear();
                    topicsCoveredTextField.clear();
                }
            } else {
                System.err.println("[CLIENT ERROR] Subject ID not found for subject: " + selectedSubject);
                objectivesTextField.clear();
                topicsCoveredTextField.clear();
            }
        }
    }

    private void handleSave() throws RemoteException {
        String acadLvl = academicLevelLabel != null ? academicLevelLabel.getText() : null;
        String subjectName = subjectComboBox.getValue();
        String objectives = objectivesTextField.getText();
        String topicsCovered = topicsCoveredTextField.getText();

        if (acadLvl == null || acadLvl.equals("Select a subject") || acadLvl.equals("Invalid academic level") ||
                subjectName == null || objectives.isEmpty() || topicsCovered.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "All fields must be filled in with valid values. Please check again.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String subjectID = controller.getSubjectIDByName(subjectName);
        if (subjectID == null) {
            JOptionPane.showMessageDialog(null,
                    "Subject ID not found for the selected subject.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate academic level against subject
        String expectedAcadLvl = controller.getAcademicLevelBySubjectName(subjectName);
        if (!acadLvl.equals(expectedAcadLvl)) {
            JOptionPane.showMessageDialog(null,
                    "Academic level does not match the subject's academic level: " + expectedAcadLvl,
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = controller.addNewLessonPlan(acadLvl, subjectID, subjectName, objectives, topicsCovered);
        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to add lesson plan.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Lesson plan added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshLessonPlanTable();
            closeWindow();
        }
    }

    private void refreshLessonPlanTable() {
        System.out.println("[CLIENT | " + new Date() + "] Triggering lesson plan table refresh...");
        if (parentController != null) {
            parentController.refreshTables();
        } else {
            System.err.println("[CLIENT] Parent controller not set, cannot refresh table.");
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