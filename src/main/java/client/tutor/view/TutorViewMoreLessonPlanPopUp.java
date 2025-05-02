package client.tutor.view;

import client.tutor.controller.TutorViewMoreListLessonPlanPopUpController;
import client.tutor.model.TutorViewMoreLessonPlanPopUpModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shared.classes.LessonPlan;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TutorViewMoreLessonPlanPopUp {
    @FXML
    private Label courseLabel;
    @FXML
    private Label subjectNoLabel;
    @FXML
    private Label subjectNameLabel;
    @FXML
    private Label objectivesLabel;
    @FXML
    private Label topicsLabel;

    private TutorViewMoreListLessonPlanPopUpController controller;
    private TutorViewMoreLessonPlanPopUpModel model;

    // No-argument constructor
    public TutorViewMoreLessonPlanPopUp() {
        // Default constructor for FXML loading
    }

    public TutorViewMoreLessonPlanPopUp(TutorViewMoreLessonPlanPopUpModel model) {
        this.model = model;
    }

    public void setModel(TutorViewMoreLessonPlanPopUpModel model) {
        this.model = model;
    }

    public void initializeController() {
        this.controller = new TutorViewMoreListLessonPlanPopUpController(this);
    }

    public void show(String lessonPlanID) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/tutor_lessonplan_view_more.fxml"));
            Parent root = loader.load();

            // Get the controller from the loader
            TutorViewMoreLessonPlanPopUp view = loader.getController();
            view.setModel(this.model); // Set the model after loading the FXML
            view.initializeController(); // Initialize the controller

            // Fetch lesson plan details using the lessonPlanID
            if (this.model != null) { // Ensure model is not null
                LessonPlan lessonPlan = model.fetchLessonPlanDetails(lessonPlanID); // Fetch lesson plan details from the model
                view.updateLessonPlanDetails(lessonPlan); // Update the view with lesson plan details
            } else {
                System.out.println("[CLIENT | "+ new Date()+ "] Model is not initialized.");
            }

            // Create a new Stage for the pop-up
            Stage stage = new Stage();
            stage.setTitle("Lesson Plan Details");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the pop-up and wait for it to close
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("[CLIENT | "+ new Date()+ "] Error fetching lesson plan details: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT | "+ new Date()+ "] Failed to load View More window.");
        }
    }

    public void updateLessonPlanDetails(LessonPlan lessonPlan) {
        try {
            if (lessonPlan != null) {
                courseLabel.setText(lessonPlan.getLessonPlanID());
                subjectNoLabel.setText(lessonPlan.getSubjectID());
                subjectNameLabel.setText(lessonPlan.getSubjectName());
                objectivesLabel.setText(lessonPlan.getObjectives());
                topicsLabel.setText(lessonPlan.getTopicsCovered());
            } else {
                System.out.println("[CLIENT | "+ new Date()+ "] Lesson Plan is null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[CLIENT | "+ new Date()+ "] Error updating lesson plan details: " + e.getMessage());
        }
    }
}