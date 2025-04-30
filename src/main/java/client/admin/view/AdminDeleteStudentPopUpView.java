package client.admin.view;

import client.admin.controller.AdminViewStudentController;
import client.admin.controller.AdminViewTutorController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import shared.classes.Student;
import shared.classes.Tutor;

public class AdminDeleteStudentPopUpView {
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
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
        System.out.println("Student to remove is set");
    }

    private void confirmChanges() {
        // Close confirmation window
        closeWindow();
        // Call the save logic in AdminViewStudentController if it's set
        if (studentController != null) {
            studentController.removeStudent(student);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
