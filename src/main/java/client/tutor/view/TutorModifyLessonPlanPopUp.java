package client.tutor.view;

import client.tutor.controller.TutorModifyLessonPlanController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.LessonPlan;

import javax.swing.*;

public class TutorModifyLessonPlanPopUp {
    @FXML private TextField objectivesTextField;
    @FXML private TextField topicsCoveredTextField;
    @FXML private Button updateLessonPlanButton;
    private LessonPlan lessonPlan;
    private TutorModifyLessonPlanController controller;

    public void initialize() {
        initializeController();
        updateLessonPlanButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing AdminModifyStudentPopUpController...");
        this.controller = new TutorModifyLessonPlanController();
        System.out.println("[CLIENT] AdminModifyStudentPopUpController successfully created.");
    }

    public void setLessonPlan(LessonPlan lessonPlan) {
        this.lessonPlan = lessonPlan;
    }

    //Validation checks if the objectives filed and topics covered field are empty
    private void handleSave() {
        String newObjectives = objectivesTextField.getText();
        String newTopicsCovered = topicsCoveredTextField.getText();

        if (lessonPlan == null ||
                newObjectives == null || newObjectives.trim().isEmpty() ||
                newTopicsCovered == null || newTopicsCovered.trim().isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "All fields must be filled in. Please complete the form.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = lessonPlan.getLessonPlanID();
        boolean success = controller.updateLessonPlanObjectives(id, newObjectives, newTopicsCovered);

        if (!success) {
            JOptionPane.showMessageDialog(null,
                    "Failed to update objectives.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Objectives updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            closeWindow(); // only close if successful
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) updateLessonPlanButton.getScene().getWindow();
        stage.close();
    }

    public void updateLessonPlanButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), updateLessonPlanButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void updateLessonPlanButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), updateLessonPlanButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}
