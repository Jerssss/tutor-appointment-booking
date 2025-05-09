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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import shared.classes.Student;
import shared.classes.TutorSession;
import shared.interfaces.TutorService;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @FXML private TextField searchStudResTextField;
    @FXML private Button refreshButton;

    private TutorViewMorePopUpModel model;
    private TutorViewSessionListController controller;
    private ObservableList<TutorSession> sessionData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT | "+ new Date()+ "] Table columns initialized successfully.");
        initialController();

        this.model = new TutorViewMorePopUpModel();

        try {
            searchStudResTextField.textProperty().addListener((obs, oldVal, newVal) -> {
                controller.searchSession(newVal);
            });
            refreshButton.setOnAction(event -> controller.loadSessionData());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializeTableColumns() {
        try {
            sessionNoColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSessionID()));
            subjectColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSubjectName()));
            sessionModeColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSessionMode()));
            dateColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSessionDate().toString()));
            sessionTimeColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSessionTime().toString()));
            durationColumn.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getSessionDuration()).asObject());
            statusColumn.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getSessionStatus()));

            viewMoreColumn.setCellFactory(col -> createViewMoreButtonCellFactory());
            studentColumn.setCellFactory(col -> createViewButtonCellFactory());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initialController() {
        this.controller = new TutorViewSessionListController(this);
    }

    public TableCell<TutorSession, String> createViewMoreButtonCellFactory() {
        return new TableCell<>() {
            private final Button viewButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                viewButton.setGraphic(iv);
                viewButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
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
                setGraphic(empty ? null : viewButton);
            }
        };
    }

    public TableCell<TutorSession, String> createViewButtonCellFactory() {
        return new TableCell<>() {
            private final Button viewButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                viewButton.setGraphic(iv);
                viewButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
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
                setGraphic(empty ? null : viewButton);
            }
        };
    }

    public void showViewStudentPane() {
        TutorSession session = sessionListTableView.getSelectionModel().getSelectedItem();
        System.out.println("[CLIENT | "+ new Date()+ "] Selected session: " + session);

        if (session != null) {
            try {
                TutorService tutorService = StudentTutorClient.getTutorService();
                List<Student> students = tutorService.getStudentsBySession(session.getSessionID());

                TutorViewStudentListPopUp studentListPopUp = new TutorViewStudentListPopUp();
                studentListPopUp.setStudents(students);
                studentListPopUp.show(session.getSessionID());
            } catch (RemoteException e) {
                e.printStackTrace();
                System.out.println("[CLIENT | "+ new Date()+ "] Error retrieving students: " + e.getMessage());
            }
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No session selected.");
        }
    }

    public void showViewMorePane(TutorSession session) {
        System.out.println("[CLIENT | "+ new Date()+ "] Selected session: " + session);

        if (session != null) {
            try {
                TutorViewMorePopUp popUp = new TutorViewMorePopUp(model);
                popUp.show(session.getSessionID());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[CLIENT | "+ new Date()+ "] Error displaying session details: " + e.getMessage());
            }
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No session selected.");
        }
    }

    public void updateTable(List<TutorSession> sessions) {
        if (sessions != null && !sessions.isEmpty()) {
            Set<String> ids = new HashSet<>();
            List<TutorSession> unique = sessions.stream()
                    .filter(s -> ids.add(s.getSessionID()))
                    .toList();

            sessionData.setAll(unique);
            sessionListTableView.setItems(sessionData);
            sessionListTableView.refresh();
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No sessions to display.");
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
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }
}
