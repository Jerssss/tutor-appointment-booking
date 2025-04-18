package client.admin.controller;

import client.admin.model.AdminAddSubjectPopUpModel;
import client.admin.view.AdminAddSubjectPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminAddSubjectPopUpController implements Initializable {
    private final AdminAddSubjectPopUpModel model;
    private AdminAddSubjectPopUpView view;
    private final AdminService service = new AdminServiceImpl();

    @FXML
    private ComboBox<String> academicLevelComboBox;
    @FXML
    private TextField subjectIDTextField;
    @FXML
    private TextField subjectNameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button addSubjectWindowButton;

    public AdminAddSubjectPopUpController() {
        this.model = new AdminAddSubjectPopUpModel(service);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminAddSubjectPopUpView(
                academicLevelComboBox,
                subjectNameTextField,
                subjectIDTextField,
                descriptionTextArea,
                addSubjectWindowButton
        );

        try {
            initializeData();
            setupEventHandlers();
            view.setupButtonHoverEffects();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeData() throws RemoteException {
        view.initializeComboBoxes();
    }

    private void setupEventHandlers() {
        view.setAddSubjectWindowButton(this::handleAddSubject);
    }


    private void handleAddSubject(ActionEvent event) {
        boolean isValid = true;
        view.clearAllErrorMessages();

        if (view.getSubjectID().isEmpty() || view.getSubjectID().isBlank()) {
            view.showErrorBelowTextField(subjectIDTextField, "Please input Subject ID.");
            isValid = false;
        }

        if (view.getSubjectName().isEmpty() || view.getSubjectName().isBlank()) {
            view.showErrorBelowTextField(subjectNameTextField, "Please input Subject Name.");
            isValid = false;
        }

        if (view.getSubjectDescrip().isEmpty() || view.getSubjectDescrip().isBlank()) {
            view.showErrorBelowTextField(subjectIDTextField, "Please input Subject Description.");
            isValid = false;
        }

        if (view.getSelectedAcademicLevel() == null ) {
            view.showErrorBelowComboBox(academicLevelComboBox, "Please select an Academic Level.");
            isValid = false;
        }

        if (!isValid) return;

        try {
            model.addNewSubject(view.getSubjectID(), view.getSubjectName(), view.getSubjectDescrip(), view.getSelectedAcademicLevel());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Subject Added");
            alert.setHeaderText(null);
            alert.setContentText("The subject has been added successfully.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Stage stage = (Stage) addSubjectWindowButton.getScene().getWindow();
                    stage.close();
                }
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_new_subject_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Subject");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Add Subject window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void addSubjectButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void addSubjectButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}