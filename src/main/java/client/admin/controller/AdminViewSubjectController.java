package client.admin.controller;

import client.admin.model.AdminViewMoreSubjectPopUpModel;
import client.admin.model.AdminViewSubjectModel;
import client.admin.view.AdminDeleteSubjectPopUpView;
import client.admin.view.AdminViewSubjectView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import server.services.AdminServiceImpl;
import shared.classes.Subject;
import shared.interfaces.AdminService;

import java.rmi.RemoteException;
import java.util.List;

public class AdminViewSubjectController {
    private final AdminViewSubjectModel model;
    private AdminViewSubjectView view;
    private static String clickedSubject;
    @FXML
    private TableView<Subject> viewResTableView;
    @FXML
    private TableColumn<Subject, String> subjectIdColumn;
    @FXML
    private TableColumn<Subject, String> subjectNameColumn;
    @FXML
    private TableColumn<Subject, String> academicLevelColumn;
    @FXML
    private TableColumn<List<String>, Void> optionColumn;
    @FXML
    private TableColumn<List<String>, Void> viewMoreColumn;
    @FXML
    private TableColumn<List<String>, Void> deleteColumn;
    @FXML
    private TableView<Subject> archivedResTableView;
    @FXML
    private TableColumn<Subject, String> archivedSubjectIdColumn;
    @FXML
    private TableColumn<Subject, String> archivedSubjectNameColumn;
    @FXML
    private TableColumn<Subject, String> archivedAcademicLevelColumn;
    @FXML
    private TableColumn<List<String>, Void> archivedViewMoreColumn;
    @FXML
    private TableColumn<List<String>, Void> archivedOptionColumn;
    @FXML
    private TableColumn<List<String>, Void> archivedDeleteColumn;
    @FXML
    private Button addSubjectButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchResTextField;

    public AdminViewSubjectController() throws RemoteException {
        this.model = new AdminViewSubjectModel();
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewSubjectView(
                viewResTableView,
                subjectIdColumn,
                subjectNameColumn,
                academicLevelColumn,
                archivedResTableView,
                archivedSubjectIdColumn,
                archivedSubjectNameColumn,
                archivedAcademicLevelColumn
        );
        displaySubjects();

        setActionAddSubjectButton(this::handleAddSubject);
        setActionRefreshButtonButton(this::handleRefreshSubject);

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSubject(newValue);
        });

        viewMoreColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button viewMoreColumnButton = new Button("View More");

            {
                viewMoreColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                viewMoreColumnButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreSubjectPopUpController viewMoreSubjectModel = null;
                    try {
                        viewMoreSubjectModel = new AdminViewMoreSubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    viewMoreSubjectModel.showWindow(clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewMoreColumnButton);
                }
            }
        });

        archivedViewMoreColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button archivedViewMoreColumnButtonButton = new Button("View More");

            {
                archivedViewMoreColumnButtonButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                archivedViewMoreColumnButtonButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreSubjectPopUpController viewMoreSubjectModel = null;
                    try {
                        viewMoreSubjectModel = new AdminViewMoreSubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    viewMoreSubjectModel.showWindow(clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(archivedViewMoreColumnButtonButton);
                }
            }
        });

        optionColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    clickedSubject = clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1");
                    AdminModifySubjectPopUpController modifySubjectController = null;
                    try {
                        modifySubjectController = new AdminModifySubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
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

        archivedOptionColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button optionButton = new Button("Option");

            {
                optionButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                optionButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    clickedSubject = clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1");
                    AdminModifySubjectPopUpController modifySubjectController = null;
                    try {
                        modifySubjectController = new AdminModifySubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
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

        deleteColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button deleteColumnButton = new Button("Delete");

            {
                deleteColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                deleteColumnButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    clickedSubject = clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1");

                    AdminDeleteSubjectPopUpController deleteSubjectPopUpController = null;
                    try {
                        deleteSubjectPopUpController = new AdminDeleteSubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    deleteSubjectPopUpController.showWindow(clickedSubject);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
//                    optionButton.setDisable("Completed".equalsIgnoreCase(status) || "In Progress".equalsIgnoreCase(status) || "Cancelled".equalsIgnoreCase(status))
                    setGraphic(deleteColumnButton);
                }
            }
        });

        archivedDeleteColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button deleteColumnButton = new Button("Delete");

            {
                deleteColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                deleteColumnButton.setOnAction(event -> {
                    clickedSubject = String.valueOf(getTableView().getItems().get(getIndex()));
                    clickedSubject = clickedSubject.replaceAll(".*subjectID=([^,]+),.*", "$1");

                    AdminDeleteSubjectPopUpController deleteSubjectPopUpController = null;
                    try {
                        deleteSubjectPopUpController = new AdminDeleteSubjectPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    deleteSubjectPopUpController.showWindow(clickedSubject);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
//                    optionButton.setDisable("Completed".equalsIgnoreCase(status) || "In Progress".equalsIgnoreCase(status) || "Cancelled".equalsIgnoreCase(status))
                    setGraphic(deleteColumnButton);
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
                    academicLevelColumn,
                    archivedResTableView,
                    archivedSubjectIdColumn,
                    archivedSubjectNameColumn,
                    archivedAcademicLevelColumn
            );
            List<Subject> subjects = model.displaySubjects();
            List<Subject> archivedSubjects = model.displayArchivedSubjects();
            ObservableList<Subject> subjectData = FXCollections.observableArrayList(subjects);
            ObservableList<Subject> archivedSubjectData = FXCollections.observableArrayList(archivedSubjects);
            view.displaySubject(subjectData, archivedSubjectData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSubject(ActionEvent event) {
        AdminAddSubjectPopUpController createSubjectController = null;
        try {
            createSubjectController = new AdminAddSubjectPopUpController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        createSubjectController.showWindow();
    }

    private void handleRefreshSubject(ActionEvent event) {
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

    public static String getClickedSubject() {
        return clickedSubject;
    }

    private void filterSubject(String searchText) {
        try {
            List<Subject> allSubjects = model.displayAllSubjects();
            ObservableList<Subject> filteredData = FXCollections.observableArrayList();
            ObservableList<Subject> filteredArchivedData = FXCollections.observableArrayList();

            if (searchText == null || searchText.isEmpty()) {
                for (Subject subject : allSubjects) {
                    if (subject.getVisibility().equals("Available")) {
                        filteredData.add(subject);
                    } else {
                        filteredArchivedData.add(subject);
                    }
                }
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (Subject subject : allSubjects) {
                    boolean match = subject.getSubjectID().toLowerCase().contains(lowerCaseSearchText) || subject.getSubjectName().toLowerCase().contains(lowerCaseSearchText) ||
                            subject.getAcademicLevel().toLowerCase().contains(lowerCaseSearchText);
                    if (match) {
                        if (subject.getVisibility().equals("Available")) {
                            filteredData.add(subject);
                        } else {
                            filteredArchivedData.add(subject);
                        }
                    }
                }
            }

            view.displaySubject(filteredData, filteredArchivedData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
