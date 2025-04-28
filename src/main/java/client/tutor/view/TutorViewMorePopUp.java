package client.tutor.view;

import client.tutor.controller.TutorViewMoreListPopUpController;
import client.tutor.model.TutorViewMorePopUpModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shared.classes.TutorSession;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TutorViewMorePopUp {
    @FXML
    private Label sessionPriceLabel;
    @FXML
    private Label maximumStudentsLabel;
    @FXML
    private Label numberOfStudentsLabel;
    @FXML
    private Label sessionTypeLabel;
    @FXML
    private Label sessionDurationLabel;
    @FXML
    private Label sessionTimeLabel;
    @FXML
    private Label sessionDateLabel;
    @FXML
    private Label sessionStatusLabel;
    @FXML
    private Label subjectNameLabel;
    @FXML
    private Label sessionNoLabel;

    private TutorViewMoreListPopUpController controller;
    private TutorViewMorePopUpModel model;


    public TutorViewMorePopUp() {
    }

    // Constructor that accepts the model
    public TutorViewMorePopUp(TutorViewMorePopUpModel model) {
        this.model = model;
    }

    // Method to set the model after instantiation
    public void setModel(TutorViewMorePopUpModel model) {
        this.model = model;
    }

    public void initializeController() {
        this.controller = new TutorViewMoreListPopUpController(this);
    }

    public void show(String sessionID) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/tutor_session_view_more.fxml"));
            Parent root = loader.load();

            // Get the controller from the loader
            TutorViewMorePopUp view = loader.getController();
            view.setModel(this.model); // Set the model after loading the FXML
            view.initializeController(); // Initialize the controller

            // Fetch session details using the sessionID
            TutorSession session = model.fetchSessionDetails(sessionID); // Fetch session details from the model
            view.updateSessionDetails(session); // Update the view with session details

            // Create a new Stage for the pop-up
            Stage stage = new Stage();
            stage.setTitle("Session Details");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the pop-up and wait for it to close
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Error fetching session details: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load View More window.");
        }
    }

    public void updateSessionDetails(TutorSession session) {
        System.out.println("sessionNoLabel: " + sessionNoLabel);
        System.out.println("sessionPriceLabel: " + sessionPriceLabel);
        try {
            if (session != null) {
                List<String> details = new ArrayList<>();
                details.add(session.getSessionID());
                details.add(session.getSubjectName());
                details.add(session.getSessionStatus());
                details.add(session.getSessionDate().toString());
                details.add(session.getSessionTime().toString());
                details.add(String.valueOf(session.getSessionDuration()));
                details.add(session.getSessionType()); // Get the session type from the session object
                details.add(String.valueOf(session.getNumberOfStudents()));
                details.add(String.valueOf(session.getMaximumStudents()));
                details.add(String.valueOf(session.getSessionPrice()));

                // Set the labels using the details list
                sessionNoLabel.setText(details.get(0));
                subjectNameLabel.setText(details.get(1));
                sessionStatusLabel.setText(details.get(2));
                sessionDateLabel.setText(details.get(3));
                sessionTimeLabel.setText(details.get(4));
                sessionDurationLabel.setText(details.get(5));
                sessionTypeLabel.setText(details.get(6)); // Set the session type label
                numberOfStudentsLabel.setText(details.get(7));
                maximumStudentsLabel.setText(details.get(8));
                sessionPriceLabel.setText("₱" + details.get(9));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Error updating session details: " + e.getMessage());
        }
    }
}