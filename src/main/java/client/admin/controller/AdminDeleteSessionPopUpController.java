package client.admin.controller;

import client.admin.model.AdminDeleteSessionPopUpModel;
import client.admin.view.AdminDeleteSessionPopUpView;
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
import java.util.ResourceBundle;

public class AdminDeleteSessionPopUpController implements Initializable {
    private final AdminDeleteSessionPopUpModel model;
    private AdminDeleteSessionPopUpView view;
    private final AdminService service = new AdminServiceImpl();

//    @FXML
//    private ComboBox<String> sessionIdComboBox;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public AdminDeleteSessionPopUpController() throws RemoteException {
        this.model = new AdminDeleteSessionPopUpModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminDeleteSessionPopUpView(
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
            String sessionID = AdminViewSessionController.getClickedSession().getSessionID();
            int output = model.deleteSession(sessionID);
            if (output == 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Deletion Successful");
                alert.setHeaderText("Session successfully Deleted.");
                alert.setContentText("The session was deleted.");
                alert.showAndWait();
            } else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Deletion Error");
                alert.setHeaderText("Cannot Delete Session");
                alert.setContentText("This session cannot be deleted because it is associated with booking records.");
                alert.showAndWait();
            }
            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/delete_session.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Delete Session");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Delete Session window: " + e.getMessage());
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

