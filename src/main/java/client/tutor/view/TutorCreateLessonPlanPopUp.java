package client.tutor.view;

import client.tutor.controller.TutorCreateLessonPlanController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;

public class TutorCreateLessonPlanPopUp {
    @FXML private ComboBox academicLevelComboBox;
    @FXML private ComboBox subjectComboBox;
    @FXML private TextField objectivesTextField;
    @FXML private TextField topicsCoveredTextField;
    @FXML private Button addLessonPlanButton;
    private TutorCreateLessonPlanController controller;

    public void initialize() {
        initializeController();
        academicLevelComboBox.getItems().addAll("High School", "College");
        subjectComboBox.getItems().addAll("IT 324", "IT 323", "IT 322",
                "IT 313", "IT 312", "IT 311",
                "IT 213", "IT 212", "IT 211",
                "IT 123", "IT 122", "IT 121",
                "IT 113", "IT 112", "IT 111",
                "HS 119", "HS 118", "HS 117",
                "HS 116", "HS 115", "HS 114",
                "HS 113", "HS 112", "HS 111",
                "222", "123", "1111");
        addLessonPlanButton.setOnAction(event -> handleSave());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing TutorCreateLessonPlanController...");
        this.controller = new TutorCreateLessonPlanController();
        System.out.println("[CLIENT] TutorCreateLessonPlanController successfully created.");
    }

    private void handleSave() {
        String acadLvl = (String) academicLevelComboBox.getValue();
        String subject = (String) subjectComboBox.getValue();
        String objectives = objectivesTextField.getText();
        String topicsCovered = topicsCoveredTextField.getText();

        //Check for empty fields or null combo box selections
        if (acadLvl == null || acadLvl.trim().isEmpty() ||
                subject == null || subject.trim().isEmpty() ||
                objectives == null || objectives.trim().isEmpty() ||
                topicsCovered == null || topicsCovered.trim().isEmpty()) {

            JOptionPane.showMessageDialog(null,
                    "All fields must be filled in. Please check again.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // All good, proceed to create and add the lesson plan
        boolean success = controller.addNewLessonPlan(acadLvl, subject, objectives, topicsCovered);

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
            closeWindow(); // only close if success
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
