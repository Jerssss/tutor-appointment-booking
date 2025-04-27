package client.admin.controller;

import client.admin.model.AdminDeleteSessionPopUpModel;
import client.admin.model.AdminDeleteSubjectPopUpModel;
import client.admin.view.AdminDeleteSessionPopUpView;
import client.admin.view.AdminDeleteSubjectPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class AdminDeleteSubjectPopUpController implements Initializable {
    private final AdminDeleteSubjectPopUpModel model;
    private AdminDeleteSubjectPopUpView view;
    private final AdminService service = new AdminServiceImpl();
    private static String subject;

    //    @FXML
//    private ComboBox<String> sessionIdComboBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public AdminDeleteSubjectPopUpController() throws RemoteException {
        this.model = new AdminDeleteSubjectPopUpModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminDeleteSubjectPopUpView(
                cancelButton,
                confirmButton
        );

        setupEventHandlers();
        view.setupButtonHoverEffects();
    }

    private void setupEventHandlers() {
        view.setCancelButton(this::handleCancel);
        view.setConfirmButton(this::handleConfirm);

    }


    private void handleConfirm(ActionEvent event) {
        try {
            int output = model.deleteSubject(subject);
            if (output == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deletion Successful");
                alert.setHeaderText("Subject successfully Deleted.");
                alert.setContentText("The subject was deleted.");
                alert.showAndWait();
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Cannot Delete Subject");
                alert.setContentText("This subject cannot be deleted because it is associated with \nsession records." +
                        " Try setting Subject Visibility to Archived\ninstead.");
                alert.showAndWait();
            }
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLIntegrityConstraintViolationException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Deletion Error");
            alert.setHeaderText("Cannot Delete Subject");
            alert.setContentText("This subject cannot be deleted because it is associated with session records.");
            alert.showAndWait();
        }
    }

    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void showWindow(String subjectID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/delete_subject.fxml"));
            Parent root = loader.load();

            subject = subjectID;
            Stage stage = new Stage();
            stage.setTitle("Delete Subject");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Delete Subject window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSessionButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void deleteSessionButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }

}

