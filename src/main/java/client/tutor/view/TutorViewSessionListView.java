package client.tutor.view;

import client.StudentTutorClient;
import client.tutor.controller.TutorViewSessionListController;
import client.tutor.model.TutorViewMorePopUpModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;
import java.rmi.RemoteException;
import java.util.List;

public class TutorViewSessionListView {
    @FXML private TableColumn<TutorSession, String> viewMoreColumn;
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
    private TutorViewMorePopUpModel model;
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

        this.model = new TutorViewMorePopUpModel();

        try {
            searchStudResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchSession(newValue);
            });
            refreshButton.setOnAction(event -> controller.loadSessionData());
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
            viewMoreColumn.setCellFactory(column -> createViewMoreButtonCellFactory());
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
                viewButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
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

    public TableCell<TutorSession, String> createViewMoreButtonCellFactory() {
        return new TableCell<TutorSession, String>() {
            private final Button viewButton = new Button("View More");

            {
                viewButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
                viewButton.setOnAction(event -> {
                    TutorSession session = getTableRow().getItem();
                    if (session != null) {
                        sessionListTableView.getSelectionModel().select(session);
                        showViewMorePane(session);
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

    public void showViewMorePane(TutorSession session) {
        System.out.println("[CLIENT] Selected session: " + session);

        if (session != null) {
            try {
                // Create a new instance of TutorViewMorePopUp using the constructor that accepts a model
                TutorViewMorePopUp tutorViewMorePopUp = new TutorViewMorePopUp(model); // Pass the model here

                // Show the pop-up and pass the session ID
                tutorViewMorePopUp.show(session.getSessionID());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[CLIENT] Error displaying session details: " + e.getMessage());
            }
        } else {
            System.out.println("[CLIENT] No session selected.");
        }
    }


    public void updateTable(List<TutorSession> sessions) {
        if (sessions != null && !sessions.isEmpty()) {
            sessionData.setAll(sessions);
            sessionListTableView.setItems(sessionData);
            sessionListTableView.refresh();
        } else {
            System.out.println("No sessions to display.");
        }
    }

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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