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
import javafx.scene.control.*;
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
    @FXML
    private TextField searchResTextField;

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
                refreshButton,
                searchResTextField
        );
        displaySessions();

        setActionAddSessionButton(this::handleAddSession);
        setActionRefreshButton(this::handleRefreshSession);

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSessions(newValue);
        });

        optionColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSession = getTableView().getItems().get(getIndex());
                    AdminModifySessionPopUpController modifySessionController= new AdminModifySessionPopUpController();
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
        AdminAddSessionPopUpController createSessionController = new AdminAddSessionPopUpController();
        createSessionController.showWindow();
    }

    private void handleRefreshSession(ActionEvent event){
        displaySessions();
    }
    private void handleSearch(ActionEvent event){
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

    private void filterSessions(String searchText) {
        try {
            List<List<String>> allSessions = model.displaySessions();
            ObservableList<List<String>> filteredData = FXCollections.observableArrayList();

            if (searchText == null || searchText.isEmpty()) {
                filteredData.addAll(allSessions);
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (List<String> session : allSessions) {
                    boolean match = session.stream()
                            .anyMatch(field -> field != null && field.toLowerCase().contains(lowerCaseSearchText));
                    if (match) {
                        filteredData.add(session);
                    }
                }
            }

            view.displaySession(filteredData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
