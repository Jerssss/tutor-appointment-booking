package client.admin.view;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class AdminModifyLessonPlanPopUpView {
    private final ComboBox<String> lessonPlanVisibilityComboBox;
    private final Button modifyLessonPlanButton;

    public AdminModifyLessonPlanPopUpView(ComboBox<String> lessonPlanVisibilityComboBox, Button modifyLessonPlanButton) {
        this.lessonPlanVisibilityComboBox = lessonPlanVisibilityComboBox;
        this.modifyLessonPlanButton = modifyLessonPlanButton;
    }

    public void initializeComboBoxes() {
        lessonPlanVisibilityComboBox.getItems().addAll("Available", "Archived");
    }

    public void setModifySubjectButton(EventHandler<ActionEvent> handler) {
        if (modifyLessonPlanButton != null) {
            modifyLessonPlanButton.setOnAction(handler);
        }
    }
    public void setupButtonHoverEffects() {
        modifyLessonPlanButton.setOnMouseEntered(this::handleButtonHover);
        modifyLessonPlanButton.setOnMouseExited(this::handleButtonExit);
    }
    public void handleButtonHover(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifyLessonPlanButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    public void handleButtonExit(MouseEvent event) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), modifyLessonPlanButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public String getSelectedLessonPlanVisibility() {
        return lessonPlanVisibilityComboBox.getValue();
    }


}