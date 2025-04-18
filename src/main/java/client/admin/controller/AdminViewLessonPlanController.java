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
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.classes.LessonPlan;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminViewLessonPlanController {
    private final AdminViewLessonPlanModel model;
    private AdminViewLessonPlanView view;
    private AdminService service = new AdminServiceImpl();
    private static String clickedLessonPlan;

    @FXML
    private TableView<LessonPlan> highSchoolTableView;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolSubjectIDColumn;
//    @FXML
//    private TableColumn<LessonPlan, String> highSchoolSubjectNameColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolObjectivesColumn;
    @FXML
    private TableColumn<LessonPlan, String> highSchoolTopicsColumn;

    @FXML
    private TableColumn<List<String>, Void> highSchoolViewMoreColumn;
    @FXML
    private TableColumn<List<String>, Void> highSchoolDeleteColumn;

    @FXML
    private TableView<LessonPlan> collegeTableView;
    @FXML
    private TableColumn<LessonPlan, String> collegeLessonPlanIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeSubjectIDColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeObjectivesColumn;
    @FXML
    private TableColumn<LessonPlan, String> collegeTopicsColumn;
    @FXML
    private TableColumn<List<String>, Void> collegeViewMoreColumn;
    @FXML
    private TableColumn<List<String>, Void> collegeDeleteColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField searchReportTextField;

    public AdminViewLessonPlanController() {
        this.model = new AdminViewLessonPlanModel(service);
    }

    @FXML
    private void initialize() {
        this.view = new AdminViewLessonPlanView(
                highSchoolTableView,
                highSchoolLessonPlanIDColumn,
                highSchoolSubjectIDColumn,
                highSchoolObjectivesColumn,
                highSchoolTopicsColumn,
                collegeTableView,
                collegeLessonPlanIDColumn,
                collegeSubjectIDColumn,
                collegeObjectivesColumn,
                collegeTopicsColumn
        );
        displayLessonPlans();
        searchReportTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLessonPlans(newValue);
        });

        highSchoolViewMoreColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button highSchoolViewMoreColumnButton = new Button("View More");

            {
                highSchoolViewMoreColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                highSchoolViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController= new AdminViewMoreLessonPlanPopUpController();
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

        highSchoolDeleteColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button highSchoolDeleteColumnButton = new Button("Delete");

            {
                highSchoolDeleteColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                highSchoolDeleteColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
//                    AdminViewMoreSubjectPopUpController viewMoreSubjectPopUpController= new AdminViewMoreSubjectPopUpController();
//                    viewMoreSubjectModel.showWindow(clickedSubject);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(highSchoolDeleteColumnButton);
                }
            }
        });

        collegeViewMoreColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button collegeViewMoreColumnButton = new Button("View More");

            {
                collegeViewMoreColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                collegeViewMoreColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
                    AdminViewMoreLessonPlanPopUpController viewMoreLessonPlanPopUpController= new AdminViewMoreLessonPlanPopUpController();
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

        collegeDeleteColumn.setCellFactory(col -> new TableCell<List<String>, Void>() {
            private final Button collegeDeleteColumnButton = new Button("Delete");

            {
                collegeDeleteColumnButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white; -fx-background-radius: 15;");
                collegeDeleteColumnButton.setOnAction(event -> {
                    clickedLessonPlan = String.valueOf(getTableView().getItems().get(getIndex()));
//                    AdminViewMoreSubjectPopUpController viewMoreSubjectPopUpController= new AdminViewMoreSubjectPopUpController();
//                    viewMoreSubjectModel.showWindow(clickedSubject);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(collegeDeleteColumnButton);
                }
            }
        });

        setActionRefreshButtonButton(this::handleRefreshLessonPlan);
    }

    private void displayLessonPlans() {
        //            List<LessonPlan> allCollegeLessonPlans = new ArrayList<>();
//            List<String> allCollegeSubjectNames = new ArrayList<>();
//            List<LessonPlan> allHighSchoolLessonPlans = new ArrayList<>();
//            List<String> allHighSchoolSubjectNames = new ArrayList<>();

//            for (Map.Entry<LessonPlan, String> entry : collegeLessonPlans.entrySet()) {
//                allCollegeLessonPlans.add(entry.getKey());
//                allCollegeSubjectNames.add(entry.getValue());
//            }
//            for (Map.Entry<LessonPlan, String> entry : highSchoolLessonPlans.entrySet()) {
//                allHighSchoolLessonPlans.add(entry.getKey());
//                allHighSchoolSubjectNames.add(entry.getValue());
//            }

        List<LessonPlan> collegeLessonPlans = model.getLessonPlanCollege();
        List<LessonPlan> highSchoolLessonPlans = model.getLessonPlanHighSchool();
        ObservableList<LessonPlan> allCollegeLessonPlansData = FXCollections.observableArrayList(collegeLessonPlans);
//            ObservableList<String> allCollegeSubjectNamesData = FXCollections.observableArrayList(allCollegeSubjectNames);
        ObservableList<LessonPlan> allHighSchoolLessonPlansData = FXCollections.observableArrayList(highSchoolLessonPlans);
//            ObservableList<String> allHighSchoolSubjectNamesData = FXCollections.observableArrayList(allHighSchoolSubjectNames);
//            view.displayLessonPlan(allCollegeLessonPlansData, allCollegeSubjectNamesData, allHighSchoolLessonPlansData, allHighSchoolSubjectNamesData);
        view.displayLessonPlan(allCollegeLessonPlansData, allHighSchoolLessonPlansData);

    }

    private void handleRefreshLessonPlan(ActionEvent event){
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

            ObservableList<LessonPlan> filteredCollegePlans = FXCollections.observableArrayList();
            ObservableList<LessonPlan> filteredHighSchoolPlans = FXCollections.observableArrayList();

            if (searchText == null || searchText.isEmpty()) {
                filteredCollegePlans.addAll(collegePlans);
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (LessonPlan plan : collegePlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredCollegePlans.add(plan);
                    }
                }
            }

            if (searchText == null || searchText.isEmpty()) {
                filteredHighSchoolPlans.addAll(highSchoolPlans);
            } else {
                String lowerCaseSearchText = searchText.toLowerCase();
                for (LessonPlan plan : highSchoolPlans) {
                    if (matchesSearch(plan, lowerCaseSearchText)) {
                        filteredHighSchoolPlans.add(plan);
                    }
                }
            }


            view.displayLessonPlan(filteredCollegePlans, filteredHighSchoolPlans);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean matchesSearch(LessonPlan plan, String searchText) {
        return plan.getLessonPlanID().toLowerCase().contains(searchText) ||
                plan.getSubjectID().toLowerCase().contains(searchText) ||
                plan.getObjectives().toLowerCase().contains(searchText) ||
                plan.getTopicsCovered().toLowerCase().contains(searchText);
    }
}
