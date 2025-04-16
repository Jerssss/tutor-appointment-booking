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
    private Button addSubjectButton;

    public AdminViewSubjectController() {
        this.model = new AdminViewSubjectModel(service);
        System.out.println("**********8");
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
            System.out.println("Number of subjects received: " + subjects.size());
            subjects.forEach(subject -> System.out.println(subject.getSubjectName()));

            ObservableList<Subject> subjectData = FXCollections.observableArrayList(subjects);
            view.displaySubject(subjectData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSubject(ActionEvent event){
//        redirectToAddSessionPopup(event);
        AdminCreateSessionController createSessionController = new AdminCreateSessionController();
        createSessionController.showWindow();
    }

    public void setActionAddSubjectButton(EventHandler<ActionEvent> event) {
        if (addSubjectButton != null) {
            addSubjectButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] logInPageSignUpButton is NULL! Check FXML.");
        }
    }
}
