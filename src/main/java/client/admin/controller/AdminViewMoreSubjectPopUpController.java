package client.admin.controller;

import client.admin.model.AdminAddSubjectPopUpModel;
import client.admin.model.AdminViewMoreSessionsPopUpModel;
import client.admin.model.AdminViewMoreSubjectPopUpModel;
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

public class AdminViewMoreSubjectPopUpController {

    private final AdminService service = new AdminServiceImpl();
    private AdminViewMoreSubjectPopUpModel model;
    private String subjectID;

    @FXML
    private Label subjectLevelLabel;
    @FXML
    private Label subjectDescriptionLabel;
    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
        this.model = new AdminViewMoreSubjectPopUpModel(service);
        setSubjectDetails();
    }

    private void setSubjectDetails() {
        try {
            List<String> details = model.getDetails(subjectID);
            if (details.size() >= 2) {
                subjectLevelLabel.setText(details.get(0));
                subjectDescriptionLabel.setText(details.get(1));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showWindow(String subjectID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/view_more_subject.fxml"));
            Parent root = loader.load();
            AdminViewMoreSubjectPopUpController controller = loader.getController();
            controller.setSubjectID(subjectID);

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
