package client.admin.controller;

import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminViewSessionView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSessionController {
    private final AdminViewSessionModel model;
    private final AdminViewSessionView view;
    private AdminService service = new AdminServiceImpl();

    @FXML
    public TableView<List<String>> viewResTableView;

    @FXML
    public TableColumn<List<String>, String> dateColumn;
    @FXML
    public TableColumn<List<String>, String> timeColumn;
    @FXML
    public TableColumn<List<String>, String> durationColumn;
    @FXML
    public TableColumn<List<String>, String> academicLevelColumn;
    @FXML
    public TableColumn<List<String>, String> subjectColumn;

    @FXML
    private void initialize() {
        displaySessions();
    }
    public AdminViewSessionController() {
        this.model = new AdminViewSessionModel(service);
        this.view = new AdminViewSessionView();
    }

    public AdminViewSessionController(AdminViewSessionModel model, AdminViewSessionView view) {
        this.model = model;
        this.view = view;
    }

    public void displaySessions() {
        // Set up table columns to specific indices in the List<List<Strinh>> sessions;
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));

        try {
            List<List<String>> sessions = model.displaySessions();
            ObservableList<List<String>> sessionData = FXCollections.observableArrayList(sessions);

            // Put the sessions in the table
            viewResTableView.setItems(sessionData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
