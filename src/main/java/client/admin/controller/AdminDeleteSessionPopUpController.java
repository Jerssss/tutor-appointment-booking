package client.admin.controller;

import client.admin.model.AdminDeleteSessionPopUpModel;
import client.admin.view.AdminDeleteSessionPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public AdminDeleteSessionPopUpController() {
        this.model = new AdminDeleteSessionPopUpModel(service);
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
            String sessionID = AdminViewSessionController.getClickedSession().getFirst();
            model.deleteSession(sessionID);
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

