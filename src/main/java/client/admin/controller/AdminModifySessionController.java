package client.admin.controller;

import client.admin.model.AdminCreateSubjectModel;
import client.admin.model.AdminModifySessionModel;
import client.admin.view.AdminCreateSubjectView;
import client.admin.view.AdminModifySessionView;
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

public class AdminModifySessionController implements Initializable {
    private final AdminModifySessionModel model;
    private AdminModifySessionView view;
    private final AdminService service = new AdminServiceImpl();

    @FXML
    private ComboBox<String> sessionTypeComboBox;
    @FXML
    private ComboBox<String> sessionModeComboBox;
    @FXML
    private Button modifySessionButton;

    public AdminModifySessionController() {
        this.model = new AdminModifySessionModel(service);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminModifySessionView(
                sessionTypeComboBox,
                sessionModeComboBox,
                modifySessionButton
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
        view.setModifySessionButton(this::handleUpdateSession);
    }


    private void handleUpdateSession(ActionEvent event) {
        try {
            System.out.println("WOAH");
            String sessionID = AdminViewSessionController.getClickedSession().get(0);
            model.updateSession(sessionID, view.getSelectedSessionMode(), view.getSelectedSessionType());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_session.fxml"));
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
    private void modifySessionButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void modifySessionButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}