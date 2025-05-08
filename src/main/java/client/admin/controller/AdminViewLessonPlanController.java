package client.admin.controller;

import client.admin.model.AdminViewLessonPlanModel;
import client.admin.model.AdminViewSessionModel;
import client.admin.view.AdminViewLessonPlanView;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.classes.LessonPlan;
import shared.classes.TutorSession;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminViewLessonPlanController {
    private final AdminViewLessonPlanModel model;
    private AdminViewLessonPlanView view;
    private static String clickedLessonPlan;

    @FXML
    private TableView<LessonPlan> highSchoolTableView;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolSubjectIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolSubjectNameColumn;
    @FXML
    private TableColumn<LessonPlan, Void> highSchoolViewMoreColumn;
    @FXML
    private TableColumn<LessonPlan, Void> highSchoolOptionColumn;


    @FXML
    private TableView<LessonPlan> highSchoolArchivedTableView;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolArchivedLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolArchivedSubjectIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolArchivedSubjectNameColumn;
    @FXML
    private TableColumn<LessonPlan, Void> highSchoolArchivedViewMoreColumn;
    @FXML
    private TableColumn<LessonPlan, Void> highSchoolArchiveOptionColumn;


    @FXML
    private TableView<LessonPlan> collegeTableView;
    @FXML
    private TableColumn<LessonPlan, String> collegeLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeSubjectIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeSubjectNameColumn;
    @FXML
    private TableColumn<LessonPlan, Void> collegeViewMoreColumn;
    @FXML
    private TableColumn<LessonPlan, Void> collegeOptionColumn;

    @FXML
    private TableView<LessonPlan> collegeArchivedTableView;
    @FXML
    private TableColumn<LessonPlan, String> collegeArchivedLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeArchivedSubjectIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeArchivedSubjectNameColumn;
    @FXML
    private TableColumn<LessonPlan, Void> collegeArchivedViewMoreColumn;
    @FXML
    private TableColumn<LessonPlan, Void> collegeArchiveOptionColumn;

    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchReportTextField;

    public AdminViewLessonPlanController() throws RemoteException {
        this.model = new AdminViewLessonPlanModel();
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewLessonPlanView(
                highSchoolTableView,
                highSchoolLessonPlanIDColumn,
                highSchoolSubjectIDColumn,
                highSchoolSubjectNameColumn,
                collegeTableView,
                collegeLessonPlanIDColumn,
                collegeSubjectIDColumn,
                collegeSubjectNameColumn,
                highSchoolArchivedTableView,
                highSchoolArchivedLessonPlanIDColumn,
                highSchoolArchivedSubjectIDColumn,
                highSchoolArchivedSubjectNameColumn,
                collegeArchivedTableView,
                collegeArchivedLessonPlanIDColumn,
                collegeArchivedSubjectIDColumn,
                collegeArchivedSubjectNameColumn
        );
        displayLessonPlans();
        searchReportTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLessonPlans(newValue);
        });

        highSchoolViewMoreColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button highSchoolViewMoreColumnButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                highSchoolViewMoreColumnButton.setGraphic(imageView);

                highSchoolViewMoreColumnButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                highSchoolViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController = new AdminViewMoreLessonPlanPopUpController();
                    viewMoreLessonPlanPopUpController.showWindow(clickedLessonPlan.replaceAll(".*lessonPlanID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(highSchoolViewMoreColumnButton);
                }
            }
        });

        highSchoolOptionColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button optionButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                optionButton.setGraphic(imageView);

                optionButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                optionButton.setOnAction(event -> {
                    clickedLessonPlan =  String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminModifyLessonPlanPopUpController modifyLessonPlanPopUpController = null;
                    try {
                        modifyLessonPlanPopUpController = new AdminModifyLessonPlanPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    modifyLessonPlanPopUpController.showWindow();
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

        highSchoolArchivedViewMoreColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button highSchoolViewMoreColumnButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                highSchoolViewMoreColumnButton.setGraphic(imageView);

                highSchoolViewMoreColumnButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                highSchoolViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController = new AdminViewMoreLessonPlanPopUpController();
                    viewMoreLessonPlanPopUpController.showWindow(clickedLessonPlan.replaceAll(".*lessonPlanID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(highSchoolViewMoreColumnButton);
                }
            }
        });

        highSchoolArchiveOptionColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button optionButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                optionButton.setGraphic(imageView);

                optionButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                optionButton.setOnAction(event -> {
                    clickedLessonPlan =  String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminModifyLessonPlanPopUpController modifyLessonPlanPopUpController = null;
                    try {
                        modifyLessonPlanPopUpController = new AdminModifyLessonPlanPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    modifyLessonPlanPopUpController.showWindow();
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

        collegeViewMoreColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button collegeViewMoreColumnButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                collegeViewMoreColumnButton.setGraphic(imageView);

                collegeViewMoreColumnButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );


                collegeViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController = new AdminViewMoreLessonPlanPopUpController();
                    viewMoreLessonPlanPopUpController.showWindow(clickedLessonPlan.replaceAll(".*lessonPlanID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(collegeViewMoreColumnButton);
                }
            }
        });

        collegeOptionColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button optionButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                optionButton.setGraphic(imageView);

                optionButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                optionButton.setOnAction(event -> {
                    clickedLessonPlan =  String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminModifyLessonPlanPopUpController modifyLessonPlanPopUpController = null;
                    try {
                        modifyLessonPlanPopUpController = new AdminModifyLessonPlanPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    modifyLessonPlanPopUpController.showWindow();
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

        collegeArchivedViewMoreColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button collegeViewMoreColumnButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                collegeViewMoreColumnButton.setGraphic(imageView);

                collegeViewMoreColumnButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                collegeViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController = new AdminViewMoreLessonPlanPopUpController();
                    viewMoreLessonPlanPopUpController.showWindow(clickedLessonPlan.replaceAll(".*lessonPlanID=([^,]+),.*", "$1"));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(collegeViewMoreColumnButton);
                }
            }
        });

        collegeArchiveOptionColumn.setCellFactory(col -> new TableCell<LessonPlan, Void>() {
            private final Button optionButton = new Button();

            {
                Image image = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(16);
                imageView.setFitHeight(16);

                optionButton.setGraphic(imageView);

                optionButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                optionButton.setOnAction(event -> {
                    clickedLessonPlan =  String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminModifyLessonPlanPopUpController modifyLessonPlanPopUpController = null;
                    try {
                        modifyLessonPlanPopUpController = new AdminModifyLessonPlanPopUpController();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    modifyLessonPlanPopUpController.showWindow();
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

        setActionRefreshButtonButton(this::handleRefreshLessonPlan);
    }

    private void displayLessonPlans() {
        List<LessonPlan> collegeLessonPlans = model.getLessonPlanCollege();
        List<LessonPlan> highSchoolLessonPlans = model.getLessonPlanHighSchool();
        List<LessonPlan> collegeArchivedLessonPlans = model.getArchivedLessonPlanCollege();
        List<LessonPlan> highSchoolArchivedLessonPlans = model.getArchivedLessonPlanHighSchool();
        ObservableList<LessonPlan> allCollegeLessonPlansData = FXCollections.observableArrayList(collegeLessonPlans);
        ObservableList<LessonPlan> allHighSchoolLessonPlansData = FXCollections.observableArrayList(highSchoolLessonPlans);
        ObservableList<LessonPlan> allCollegeArchivedLessonPlansData = FXCollections.observableArrayList(collegeArchivedLessonPlans);
        ObservableList<LessonPlan> allHighSchoolArchivedLessonPlansData = FXCollections.observableArrayList(highSchoolArchivedLessonPlans);
        view.displayLessonPlan(allCollegeLessonPlansData, allHighSchoolLessonPlansData, allCollegeArchivedLessonPlansData, allHighSchoolArchivedLessonPlansData);

    }

    private void handleRefreshLessonPlan(ActionEvent event) {
        displayLessonPlans();
    }

    public void setActionRefreshButtonButton(EventHandler<ActionEvent> event) {
        if (refreshButton != null) {
            refreshButton.setOnAction(event);
        } else {
            System.err.println("[ERROR] AddSubjectButton is NULL! Check FXML.");
        }
    }

    private void filterLessonPlans(String searchText) {
        try {
            List<LessonPlan> collegePlans = model.getLessonPlanCollege();
            List<LessonPlan> highSchoolPlans = model.getLessonPlanHighSchool();
            List<LessonPlan> collegeArchivedLessonPlans = model.getArchivedLessonPlanCollege();
            List<LessonPlan> highSchoolArchivedLessonPlans = model.getArchivedLessonPlanHighSchool();
            ObservableList<LessonPlan> filteredCollegePlans = FXCollections.observableArrayList();
            ObservableList<LessonPlan> filteredHighSchoolPlans = FXCollections.observableArrayList();
            ObservableList<LessonPlan> filteredCollegeArchivedLessonPlansData = FXCollections.observableArrayList();
            ObservableList<LessonPlan> filteredHighSchoolArchivedLessonPlansData = FXCollections.observableArrayList();

            if (searchText == null || searchText.isEmpty()) {
                filteredCollegePlans.addAll(collegePlans);
                filteredHighSchoolPlans.addAll(highSchoolPlans);
                filteredCollegeArchivedLessonPlansData.addAll(collegeArchivedLessonPlans);
                filteredHighSchoolArchivedLessonPlansData.addAll(highSchoolArchivedLessonPlans);
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (LessonPlan plan : collegePlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredCollegePlans.add(plan);
                    }
                }
                for (LessonPlan plan : highSchoolPlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredHighSchoolPlans.add(plan);
                    }
                }
                for (LessonPlan plan : collegeArchivedLessonPlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredCollegeArchivedLessonPlansData.add(plan);
                    }
                }
                for (LessonPlan plan : highSchoolArchivedLessonPlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredHighSchoolArchivedLessonPlansData.add(plan);
                    }
                }
            }

            view.displayLessonPlan(filteredCollegePlans, filteredHighSchoolPlans, filteredCollegeArchivedLessonPlansData, filteredHighSchoolArchivedLessonPlansData);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean matchesSearch(LessonPlan plan, String searchText) {
        return plan.getLessonPlanID().toLowerCase().contains(searchText) ||
                plan.getSubjectID().toLowerCase().contains(searchText) ||
                plan.getSubjectName().toLowerCase().contains(searchText) ||
                plan.getObjectives().toLowerCase().contains(searchText) ||
                plan.getTopicsCovered().toLowerCase().contains(searchText) ||
                plan.getVisibility().toLowerCase().contains(searchText);

    }

    public static String getClickedLessonPlan() {
        return clickedLessonPlan;
    }
}
