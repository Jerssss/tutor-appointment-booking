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
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private Button refreshButton;

    public AdminViewLessonPlanController() {
        this.model = new AdminViewLessonPlanModel(service);
    }

//    public AdminViewLessonPlanController(AdminViewLessonPlanModel model) {
//        this.model = model;
//    }

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

}
