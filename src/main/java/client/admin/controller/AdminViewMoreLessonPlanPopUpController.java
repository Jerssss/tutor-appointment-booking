package client.admin.controller;

import client.admin.model.AdminViewMoreLessonPlanPopUpModel;
import client.admin.model.AdminViewMoreSubjectPopUpModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewMoreLessonPlanPopUpController {

    private AdminViewMoreLessonPlanPopUpModel model;
    private String lessonPlanID;

    @FXML
    private Label subjectNameLabel;
    @FXML
    private Label objectivesLabel;
    @FXML
    private Label topicsLabel;

    public void setLessonPlanID(String lessonPlanID) {
        this.lessonPlanID = lessonPlanID;
        this.model = new AdminViewMoreLessonPlanPopUpModel();
        setlessonPlanDetails();
    }

    private void setlessonPlanDetails() {
        try {
            List<String> details = model.getDetails(lessonPlanID);
            if (details.size() >= 2) {
                objectivesLabel.setText(details.get(0));
                topicsLabel.setText(details.get(1));
                subjectNameLabel.setText(details.get(2));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showWindow(String lessonPlanID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/view_more_lesson_plan.fxml"));
            Parent root = loader.load();
            AdminViewMoreLessonPlanPopUpController controller = loader.getController();
            controller.setLessonPlanID(lessonPlanID);

            Stage stage = new Stage();
            stage.setTitle("Additional Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Add Subject window: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
