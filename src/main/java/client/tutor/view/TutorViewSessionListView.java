package client.tutor.view;

import client.StudentTutorClient;
import client.tutor.controller.TutorViewSessionListController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import java.util.LinkedHashMap;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class TutorViewSessionListView {
    @FXML private TableColumn<TutorSession, String> sessionTimeColumn;
    @FXML private TableView<TutorSession> sessionListTableView;
    @FXML private TableColumn<TutorSession, String> sessionNoColumn;
    @FXML private TableColumn<TutorSession, String> subjectColumn;
    @FXML private TableColumn<TutorSession, String> sessionModeColumn;
    @FXML private TableColumn<TutorSession, String> dateColumn;
    @FXML private TableColumn<TutorSession, Integer> durationColumn;
    @FXML private TableColumn<TutorSession, String> statusColumn;
    @FXML private TableColumn<TutorSession, String> studentColumn;
    @FXML
    private TextField searchStudResTextField;
    @FXML
    private Button refreshButton;
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
        try {
            searchStudResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchSession(newValue);
            });
            refreshButton.setOnAction(event -> controller.loadSessions());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the table columns and sets up how data should be displayed.
     */
    private void initializeTableColumns() {
        try {
            sessionNoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionID()));
            subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName())); // String
            sessionModeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionMode()));
            dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionDate().toString())); // String
            sessionTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionTime().toString())); // String
            durationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSessionDuration()).asObject()); // Integer
            statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionStatus())); // String
            studentColumn.setCellFactory(column -> createViewButtonCellFactory());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initialController() {
        this.controller = new TutorViewSessionListController(this);
    }

    public TableCell<TutorSession, String> createViewButtonCellFactory() {
        return new TableCell<TutorSession, String>() {
            private final Button viewButton = new Button("View Students");

            {
                viewButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                viewButton.setOnAction(event -> {
                    TutorSession session = getTableRow().getItem();
                    if (session != null) {
                        sessionListTableView.getSelectionModel().select(session);
                        showViewStudentPane();
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        };
    }

    public void showViewStudentPane() {
        TutorSession session = sessionListTableView.getSelectionModel().getSelectedItem();
        System.out.println("[CLIENT] Selected session: " + session);

        if (session != null) {
            try {
                TutorService tutorService = StudentTutorClient.getTutorService();
                List<Student> students = tutorService.getStudentsBySession(session.getSessionID());

                TutorViewStudentListPopUp studentListPopUp = new TutorViewStudentListPopUp();

                studentListPopUp.setStudents(students);
                studentListPopUp.show(session.getSessionID());
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("[CLIENT] Error retrieving students: " + e.getMessage());
            }
        } else {
            System.out.println("[CLIENT] No session selected.");
        }
    }


    public void updateTable(List<TutorSession> sessions) {
        try {
            List<TutorSession> uniqueSessions = sessions.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    TutorSession::getSessionID,
                                    session -> session,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new
                            ),
                            map -> map.values().stream().toList()
                    ));
            sessionData.setAll(uniqueSessions);
            sessionListTableView.setItems(sessionData);
            sessionListTableView.refresh();
        } catch (Exception e) {
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