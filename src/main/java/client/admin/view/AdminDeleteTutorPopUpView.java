package client.admin.view;

import client.admin.controller.AdminViewStudentController;
import client.admin.controller.AdminViewTutorController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import shared.classes.Student;
import shared.classes.Tutor;

public class AdminDeleteTutorPopUpView {
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    private Tutor tutor;

    private AdminViewTutorController tutorController;

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> closeWindow());
        confirmButton.setOnAction(event -> confirmChanges());
    }

    public void setTutorController(AdminViewTutorController tutorController) {
        this.tutorController = tutorController;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    private void confirmChanges() {
        // Close confirmation window
        closeWindow();

        // Call the approval logic in AdminViewTutorController if it's set
        if (tutorController != null) {
            if (tutorController != null) {
                if (tutorController.removeTutor(tutor)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deletion Successful");
                    alert.setHeaderText("Tutor successfully Deleted.");
                    alert.setContentText("The tutor was deleted.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deletion Error");
                    alert.setHeaderText("Cannot Delete Tutor");
                    alert.setContentText("This tutor cannot be deleted because it is associated with \nbooking records." +
                            " Try setting Tutor Visibility to Archived\ninstead.");
                    alert.showAndWait();
                }
            }

        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
