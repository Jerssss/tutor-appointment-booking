package client.admin.controller;

import client.admin.model.AdminModifyLessonPlanPopUpModel;
import client.admin.view.AdminModifyLessonPlanPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminModifyLessonPlanPopUpController implements Initializable {
    private final AdminModifyLessonPlanPopUpModel model;
    private AdminModifyLessonPlanPopUpView view;

    @FXML
    private ComboBox<String> lessonPlanVisibilityComboBox;
    @FXML
    private Button modifyLessonPlanButton;

    public AdminModifyLessonPlanPopUpController() throws RemoteException {
        this.model = new AdminModifyLessonPlanPopUpModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminModifyLessonPlanPopUpView(
                lessonPlanVisibilityComboBox,
                modifyLessonPlanButton
        );

        try {
            initializeData();
            setupEventHandlers();
            view.setupButtonHoverEffects();
            if (!lessonPlanVisibilityComboBox.getItems().isEmpty()) {
                lessonPlanVisibilityComboBox.getSelectionModel().select(0);
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeData() throws RemoteException {
        view.initializeComboBoxes();
    }

    private void setupEventHandlers() {
        view.setModifySubjectButton(this::handleUpdateSubject);
    }


    private void handleUpdateSubject(ActionEvent event) {
        try {
            String lessonPlanID = AdminViewLessonPlanController.getClickedLessonPlan().replaceAll(".*lessonPlanID=([^,]+),.*", "$1");
            System.out.println("LESSON PLAN: " +  lessonPlanID);
            model.updateLessonPlan(lessonPlanID, view.getSelectedLessonPlanVisibility());

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Subject Updated");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The subject was successfully updated.");

            successAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ((Stage) modifyLessonPlanButton.getScene().getWindow()).close();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_lesson_plan.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Edit Session");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Edit Session window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void modifyLessonPlanButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void modifyLessonPlanButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}