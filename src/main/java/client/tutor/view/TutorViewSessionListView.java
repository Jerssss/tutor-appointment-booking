package client.tutor.view;

import client.tutor.controller.TutorViewSessionListController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.TutorSession;

import java.io.IOException;
import java.util.List;

public class TutorViewSessionListView {
    public Button refreshButton;
    @FXML private TableColumn<TutorSession, String> sessionTimeColumn;
    @FXML private TableView<TutorSession> sessionListTableView;
    @FXML private TableColumn<TutorSession, String> sessionNoColumn;
    @FXML private TableColumn<TutorSession, String> subjectColumn;
    @FXML private TableColumn<TutorSession, String> sessionModeColumn;
    @FXML private TableColumn<TutorSession, String> dateColumn;
    @FXML private TableColumn<TutorSession, Integer> durationColumn;
    @FXML private TableColumn<TutorSession, String> statusColumn;
    @FXML private TableColumn<TutorSession, Button> studentColumn;
    private TutorViewSessionListController controller;
    private ObservableList<TutorSession> sessionData = FXCollections.observableArrayList();

    /**
     * Initializes the view by setting up the table columns.
     * This runs automatically when the FXML file is loaded.
     */
    @FXML
    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT] Table columns initialized successfully.");
        initialController();
    }

    /**
     * Initializes the table columns and sets up how data should be displayed.
     */
    private void initializeTableColumns() {
        sessionNoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionID()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName())); // String
        sessionModeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionMode()));        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionDate())); // String
        sessionTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionTime())); // String
        durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSessionDuration()).asObject()); // Integer
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionStatus())); // String

        // Add the "View Student" button in the last column
        studentColumn.setCellValueFactory(cellData -> {
            Button viewStudentButton = new Button("View Student");
            viewStudentButton.setOnAction(event -> handleViewStudent(cellData.getValue()));
            return new SimpleObjectProperty<>(viewStudentButton);
        });
    }

    private void initialController() {
        this.controller = new TutorViewSessionListController(this);
    }

    public void updateTable(List<TutorSession> sessions) {
        sessionData.setAll(sessions); // Update dataset
        sessionListTableView.setItems(sessionData); // Reload table data
        sessionListTableView.refresh(); // Force UI refresh
        System.out.println("[CLIENT] Session data updated. New table size: " + sessionData.size());
    }

    private void handleViewStudent(TutorSession session) {
        try {
            // Fixed the FXML path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/student_list_pane.fxml"));
            Parent root = loader.load();

            // Show the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) sessionListTableView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("FXML not found! Check your resource path.");
            e.printStackTrace();
        }
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
