package client.admin.controller;

import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminViewSessionView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSessionController {
    private final AdminViewSessionModel model;
    private AdminViewSessionView view;
    private AdminService service = new AdminServiceImpl();
    private static List<String> clickedSession;

    @FXML
    private TableView<List<String>> viewResTableView;
    @FXML
    private TableColumn<List<String>, String> sessionIDColumn;
    @FXML
    private TableColumn<List<String>, String> dateColumn;
    @FXML
    private TableColumn<List<String>, String> timeColumn;
    @FXML
    private TableColumn<List<String>, String> durationColumn;
    @FXML
    private TableColumn<List<String>, String> academicLevelColumn;
    @FXML
    private TableColumn<List<String>, String> subjectColumn;
    @FXML
    private TableColumn<List<String>, Void> optionColumn;

    @FXML
    private Button addSessionButton;
    @FXML
    private Button refreshButton;

    public AdminViewSessionController() {
        this.model = new AdminViewSessionModel(service);
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewSessionView(
                viewResTableView,
                sessionIDColumn,
                dateColumn,
                timeColumn,
                durationColumn,
                academicLevelColumn,
                subjectColumn,
                addSessionButton,
                refreshButton
        );
        displaySessions();

        setActionAddSessionButton(this::handleAddSession);
        setActionRefreshButton(this::handleRefreshSession);

        optionColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSession = getTableView().getItems().get(getIndex());
                    System.out.println("Option clicked for session: " + clickedSession);
                    AdminModifySessionController modifySessionController= new AdminModifySessionController();
                    modifySessionController.showWindow();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(optionButton);
                }
            }
        });
    }

    private void displaySessions() {
        try {
            List<List<String>> sessions = model.displaySessions();
            ObservableList<List<String>> sessionData = FXCollections.observableArrayList(sessions);
            view.displaySession(sessionData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSession(ActionEvent event){
        AdminCreateSessionController createSessionController = new AdminCreateSessionController();
        createSessionController.showWindow();
    }

    private void handleRefreshSession(ActionEvent event){
        System.out.println("REFRESH CLICKED");
        displaySessions();
    }

    public void setActionAddSessionButton(EventHandler<ActionEvent> event) {
        if (addSessionButton != null) {
            addSessionButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageSignUpButton is NULL! Check FXML.");
        }
    }

    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        if (refreshButton != null) {
            refreshButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageSignUpButton is NULL! Check FXML.");
        }
    }

    public static List<String> getClickedSession(){
        return clickedSession;
    }
}
