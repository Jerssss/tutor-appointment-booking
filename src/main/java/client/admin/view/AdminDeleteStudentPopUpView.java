package client.admin.view;

import client.admin.controller.AdminViewStudentController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import shared.classes.Student;

import java.util.Date;

public class AdminDeleteStudentPopUpView {
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label messageLabel; // Add reference to the message label
    private Student student;
    private AdminViewStudentController studentController;

    @FXML
    private void initialize() {
        cancelButton.setOnAction(event -> closeWindow());
        confirmButton.setOnAction(event -> confirmChanges());
    }

    public void setStudentController(AdminViewStudentController studentController) {
        this.studentController = studentController;
    }

    public void setStudent(Student student) {
        this.student = student;
        System.out.println("[ADMIN CLIENT | " + new Date() + "] Student to remove is set");
        // Update the message based on balance
        if (student.getBalance() > 0) {
            messageLabel.setText("This student still has a remaining balance. Are you sure you want to delete this student?");
        } else {
            messageLabel.setText("Are you sure you want to delete this student?");
        }
    }

    private void confirmChanges() {
        closeWindow();
        if (studentController != null) {
            if (studentController.removeStudent(student)){
                System.out.println("DJSABJDSBAJBDSA");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deletion Successful");
                alert.setHeaderText("Student successfully Deleted.");
                alert.setContentText("The student was deleted.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Cannot Delete Student");
                alert.setContentText("This student cannot be deleted because it is associated with \nbooking records." +
                        " Try setting Student Visibility to Archived\ninstead.");
                alert.showAndWait();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}