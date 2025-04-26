package client.admin.controller;

import client.admin.model.AdminAddSubjectPopUpModel;
import client.admin.model.AdminViewMoreSessionsPopUpModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminViewMoreSessionsPopUpController {

    private final AdminService service = new AdminServiceImpl();
    private AdminViewMoreSessionsPopUpModel model;
    private String sessionID;

    @FXML
    private Label sessionTypeLabel;
    @FXML
    private Label sessionModeLabel;
    @FXML
    private Label tutorNameLabel;
    @FXML
    private Label subjectNameLabel;
    @FXML
    private Label academicLevelLabel;
    @FXML
    private Label numberOfStudentsLabel;
    @FXML
    private Label maxStudentsLabel;
    @FXML
    private Label sessionPriceLabel;

    public AdminViewMoreSessionsPopUpController() throws RemoteException {
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
        this.model = new AdminViewMoreSessionsPopUpModel();
        setSessionDetails();
    }

    private void setSessionDetails() {
        try {
            List<String> details = model.getDetails(sessionID);
            if (details.size() >= 8) {
                tutorNameLabel.setText(details.get(0));
                subjectNameLabel.setText(details.get(1));
                academicLevelLabel.setText(details.get(2));
                sessionTypeLabel.setText(details.get(3));
                sessionModeLabel.setText(details.get(4));
                numberOfStudentsLabel.setText(String.valueOf(details.get(5)));
                maxStudentsLabel.setText(String.valueOf(details.get(6)));
                sessionPriceLabel.setText("₱" + details.get(7));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showWindow(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/view_more_session.fxml"));
            Parent root = loader.load();
            AdminViewMoreSessionsPopUpController controller = loader.getController();
            controller.setSessionID(sessionID);

            Stage stage = new Stage();
            stage.setTitle("Additional Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Add Subject window: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
