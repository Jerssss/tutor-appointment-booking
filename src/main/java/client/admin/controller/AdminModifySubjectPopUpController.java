package client.admin.controller;

import client.admin.model.AdminModifySubjectPopUpModel;
import client.admin.view.AdminModifySubjectPopUpView;
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

public class AdminModifySubjectPopUpController implements Initializable {
    private final AdminModifySubjectPopUpModel model;
    private AdminModifySubjectPopUpView view;
    private final AdminService service = new AdminServiceImpl();

    @FXML
    private ComboBox<String> academicLevelComboBox;
    @FXML
    private Button modifySubjectButton;

    public AdminModifySubjectPopUpController() {
        this.model = new AdminModifySubjectPopUpModel(service);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminModifySubjectPopUpView(
                academicLevelComboBox,
                modifySubjectButton
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
        view.setModifySubjectButton(this::handleUpdateSubject);
    }


    private void handleUpdateSubject(ActionEvent event) {
        try {
            System.out.println("WOAH");
            String subjectID = AdminViewSubjectController.getClickedSubject();
            model.updateSession(subjectID, view.getSelectedAcademicLevel());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_subject.fxml"));
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
    private void modifySubjectButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void modifySubjectButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}