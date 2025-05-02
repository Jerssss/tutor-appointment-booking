package client.tutor.view;

import client.StudentTutorClient;
import client.tutor.controller.TutorViewStudentListPopUpController;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TutorViewStudentListPopUp {
    @FXML public Button refreshButton;
    @FXML public TextField searchTextField;
    @FXML private TableView<Student> studentTableView;
    @FXML private TableColumn<Student, String> studentIdColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();


    private List<Student> students;
    private TutorSession session;
    private TutorViewStudentListPopUpController controller;


    public TutorViewStudentListPopUp() {
    }

    public void initializeController(String sessionID) {
        System.out.println("[CLIENT | "+ new Date()+ "] Initializing TutorViewStudentListPopUpController...");
        this.controller = new TutorViewStudentListPopUpController(this, sessionID);
        System.out.println("[CLIENT | "+ new Date()+ "] TutorViewStudentListPopUpController successfully created.");

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.controller.searchStudents(newValue);
        });
        refreshButton.setOnAction(event -> controller.loadStudents(sessionID));
    }

    public void show(String sessionID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/student_list_pane.fxml"));
            Parent root = loader.load();

            TutorViewStudentListPopUp view = loader.getController();
            view.setStudents(students);
            view.setSession(session);
            view.initializeController(sessionID);

            Stage stage = new Stage();
            stage.setTitle("View Students");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT | "+ new Date()+ "] Failed to load View Student window.");
        }
    }

    @FXML
    public void initialize() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        if (students != null) {
            studentTableView.getItems().setAll(students);
        }
        try {
            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchStudents(newValue);
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void setSession(TutorSession session) {
        this.session = session;
        loadStudents(); // Load students when the session is set
    }

    private void loadStudents() {
        if (session != null) {
            List<Student> students = getStudentsBySession(session.getSessionID());
            studentTableView.getItems().setAll(students);
        }
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        if (studentTableView != null) {
            studentTableView.getItems().setAll(students);
        }
    }

    public List<Student> getStudentsBySession(String sessionID) {
        try {
            TutorService tutorService = StudentTutorClient.getTutorService();
            return tutorService.getStudentsBySession(sessionID);
        } catch (RemoteException e) {
            e.printStackTrace();
            System.out.println("[CLIENT | "+ new Date()+ "] Failed to retrieve students for session ID: " + sessionID);
            return Collections.emptyList();
        }
    }

    public void updateTable(List<Student> data) {
        studentData.setAll(data); // Update dataset
        studentTableView.setItems(null); // Force reset
        studentTableView.setItems(studentData); // Reload table data
        studentTableView.refresh(); // Force UI refresh
    }

    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}