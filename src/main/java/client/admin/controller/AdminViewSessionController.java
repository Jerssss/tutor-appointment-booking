package client.admin.controller;

import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminViewSessionView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.services.AdminServiceImpl;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;
import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSessionController {
    private final AdminViewSessionModel model;
    private AdminViewSessionView view;
    private AdminService service = new AdminServiceImpl();
    private static TutorSession clickedSession;

    @FXML
    private TableView<TutorSession> viewResTableView;
    @FXML
    private TableColumn<TutorSession, String> sessionIDColumn;
    @FXML
    private TableColumn<TutorSession, String> dateColumn;
    @FXML
    private TableColumn<TutorSession, String> timeColumn;
    @FXML
    private TableColumn<TutorSession, String> durationColumn;
    @FXML
    private TableColumn<TutorSession, String> tutorIDColumn;
    @FXML
    private TableColumn<TutorSession, String> subjectColumn;
    @FXML
    private TableColumn<TutorSession, String> statusColumn;
    @FXML
    private TableColumn<TutorSession, Void> optionColumn;
    @FXML
    private TableColumn<TutorSession, Void> viewMoreColumn;
    @FXML
    private TableColumn<TutorSession, Void> deleteColumn;
    @FXML
    private Button addSessionButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchResTextField;

    public AdminViewSessionController() throws RemoteException {
        this.model = new AdminViewSessionModel();
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewSessionView(
                viewResTableView,
                sessionIDColumn,
                dateColumn,
                timeColumn,
                durationColumn,
                tutorIDColumn,
                subjectColumn,
                statusColumn
        );
        displaySessions();

        setActionAddSessionButton(this::handleAddSession);
        setActionRefreshButton(this::handleRefreshSession);

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSessions(newValue);
        });

        viewMoreColumn.setCellFactory(col -> new TableCell<TutorSession, Void>() {
            private final Button viewMoreButton = new Button("View More");

            {
                viewMoreButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                viewMoreButton.setOnAction(event -> {
                    clickedSession = getTableView().getItems().get(getIndex());
                    AdminViewMoreSessionsPopUpController adminViewMoreSessionsPopUpController= null;
                    try {
                        adminViewMoreSessionsPopUpController = new AdminViewMoreSessionsPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    adminViewMoreSessionsPopUpController.showWindow(clickedSession.getSessionID());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewMoreButton);
                }
            }
        });

        optionColumn.setCellFactory(col -> new TableCell<TutorSession, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSession = getTableView().getItems().get(getIndex());
                    AdminModifySessionPopUpController modifySessionController = null;
                    try {
                        modifySessionController = new AdminModifySessionPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    modifySessionController.showWindow();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
//                    TutorSession sessionData = getTableView().getItems().get(getIndex());
//                    String status = sessionData.getSessionStatus();
//                    optionButton.setDisable("Completed".equalsIgnoreCase(status) || "In Progress".equalsIgnoreCase(status) || "Cancelled".equalsIgnoreCase(status));
                    setGraphic(optionButton);
                }
            }
        });

        deleteColumn.setCellFactory(col -> new TableCell<TutorSession, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                deleteButton.setOnAction(event -> {
                    clickedSession = getTableView().getItems().get(getIndex());
                    AdminDeleteSessionPopUpController deleteSessionPopUpController= null;
                    try {
                        deleteSessionPopUpController = new AdminDeleteSessionPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    deleteSessionPopUpController.showWindow();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        }

        private void displaySessions() {
        try {
            List<TutorSession> sessions = model.displaySessions();
            ObservableList<TutorSession> sessionData = FXCollections.observableArrayList(sessions);
            view.displaySession(sessionData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSession(ActionEvent event){
        AdminAddSessionPopUpController createSessionController = null;
        try {
            createSessionController = new AdminAddSessionPopUpController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        createSessionController.showWindow();
    }

    private void handleRefreshSession(ActionEvent event){
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

    public static TutorSession getClickedSession(){
        return clickedSession;
    }

    private void filterSessions(String searchText) {
        try {
            List<TutorSession> allSessions = model.displaySessions();
            ObservableList<TutorSession> filteredData = FXCollections.observableArrayList();

            if (searchText == null || searchText.isEmpty()) {
                filteredData.addAll(allSessions);
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (TutorSession session : allSessions) {
                    boolean match =
                            (session.getSessionID() != null && session.getSessionID().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getTutorID() != null && session.getTutorID().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getSubjectID() != null && session.getSubjectID().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getSubjectName() != null && session.getSubjectName().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getAcademicLevel() != null && session.getAcademicLevel().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getSessionStatus() != null && session.getSessionStatus().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getSessionMode() != null && session.getSessionMode().toLowerCase().contains(lowerCaseSearchText)) ||
                                    (session.getSessionType() != null && session.getSessionType().toLowerCase().contains(lowerCaseSearchText)) ||
                                    String.valueOf(session.getSessionPrice()).contains(lowerCaseSearchText) ||
                                    String.valueOf(session.getSessionDuration()).contains(lowerCaseSearchText) ||
                                    String.valueOf(session.getNumberOfStudents()).contains(lowerCaseSearchText) ||
                                    String.valueOf(session.getMaximumStudents()).contains(lowerCaseSearchText) ||
                                    (session.getSessionDate() != null && session.getSessionDate().toString().contains(lowerCaseSearchText)) ||
                                    (session.getSessionTime() != null && session.getSessionTime().toString().contains(lowerCaseSearchText));

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
