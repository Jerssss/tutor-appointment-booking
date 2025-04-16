package client.admin.controller;

import client.admin.model.AdminViewSubjectModel;
import client.admin.view.AdminViewSubjectView;
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
import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSubjectController {
    private final AdminViewSubjectModel model;
    private AdminViewSubjectView view;
    private AdminService service = new AdminServiceImpl();
    private static String clickedSubject;
    @FXML
    private TableView<Subject> viewResTableView;
    @FXML
    private TableColumn<Subject, String> subjectIdColumn;
    @FXML
    private TableColumn<Subject, String> subjectNameColumn;
    @FXML
    private TableColumn<Subject, String> subjectDescColumn;
    @FXML
    private TableColumn<Subject, String> academicLevelColumn;
    @FXML
    private TableColumn<List<String>, Void> optionColumn;
    @FXML
    private Button addSubjectButton;
    @FXML
    private Button refreshButton;

    public AdminViewSubjectController() {
        this.model = new AdminViewSubjectModel(service);
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewSubjectView(
                viewResTableView,
                subjectIdColumn,
                subjectNameColumn,
                subjectDescColumn,
                academicLevelColumn,
                addSubjectButton
        );
        displaySubjects();

        setActionAddSubjectButton(this::handleAddSubject);
        setActionRefreshButtonButton(this::handleRefreshSubject);

        optionColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminModifySubjectController modifySubjectController= new AdminModifySubjectController();
                    modifySubjectController.showWindow();
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

    public void displaySubjects() {
        try {
            this.view = new AdminViewSubjectView(
                    viewResTableView,
                    subjectIdColumn,
                    subjectNameColumn,
                    subjectDescColumn,
                    academicLevelColumn,
                    addSubjectButton
            );
            List<Subject> subjects = model.displaySubjects();
            ObservableList<Subject> subjectData = FXCollections.observableArrayList(subjects);
            view.displaySubject(subjectData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSubject(ActionEvent event){
        AdminCreateSubjectController createSubjectController = new AdminCreateSubjectController();
        createSubjectController.showWindow();
    }

    private void handleRefreshSubject(ActionEvent event){
        displaySubjects();
    }

    public void setActionAddSubjectButton(EventHandler<ActionEvent> event) {
        if (addSubjectButton != null) {
            addSubjectButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] AddSubjectButton is NULL! Check FXML.");
        }
    }
    public void setActionRefreshButtonButton(EventHandler<ActionEvent> event) {
        if (refreshButton != null) {
            refreshButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] AddSubjectButton is NULL! Check FXML.");
        }
    }

    public static String getClickedSubject(){
        return clickedSubject;
    }
}
