package client.student.view;

import client.student.controller.ViewLessonPlanController;
import client.student.model.ViewLessonPlanModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.LessonPlan;
import shared.interfaces.StudentService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ViewLessonPlanView implements Initializable {
    @FXML private TextField searchLessonPlanTextField;
    @FXML private Button refreshButton;
    @FXML private TableView<LessonPlan> highSchoolTableView; // High school table
    @FXML private TableColumn<LessonPlan, String> highschoolCourseColumn;
    @FXML private TableColumn<LessonPlan, String> highschoolSubjectColumn;
    @FXML private TableColumn<LessonPlan, String> highschoolObjectivesColumn;
    @FXML private TableColumn<LessonPlan, String> highschoolTopicsColumn;

    @FXML private TableView<LessonPlan> collegeTableView; // College table
    @FXML private TableColumn<LessonPlan, String> collegeCourseColumn;
    @FXML private TableColumn<LessonPlan, String> collegeSubjectColumn;
    @FXML private TableColumn<LessonPlan, String> collegeObjectivesColumn;
    @FXML private TableColumn<LessonPlan, String> collegeTopicsColumn;

    private final ObservableList<LessonPlan> allHighSchoolLessonPlans = FXCollections.observableArrayList();
    private final ObservableList<LessonPlan> allCollegeLessonPlans = FXCollections.observableArrayList();
    private ViewLessonPlanController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        try {
            initializeController();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        initializeSearchListener();
    }

    private void initializeTableColumns() {
        // High school table columns
        highschoolCourseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        highschoolSubjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID())); // Adjust as necessary
        highschoolObjectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        highschoolTopicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopicsCovered()));

        // College table columns
        collegeCourseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        collegeSubjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID())); // Adjust as necessary
        collegeObjectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        collegeTopicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopicsCovered()));
    }

    private void initializeController() throws RemoteException {
        StudentService service = new StudentServiceImpl(); // Initialize the service
        ViewLessonPlanModel model = new ViewLessonPlanModel(service);
        this.controller = new ViewLessonPlanController(this, model);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
    }

    public void updateTable(List<LessonPlan> highSchoolLessonPlans, List<LessonPlan> collegeLessonPlans) {
        if (highSchoolLessonPlans == null || highSchoolLessonPlans.isEmpty()) {
            System.out.println("[CLIENT | "+ new Date()+ "] No high school lesson plans to display.");
        } else {
            allHighSchoolLessonPlans.setAll(highSchoolLessonPlans);
            highSchoolTableView.setItems(allHighSchoolLessonPlans);
            highSchoolTableView.refresh();
            System.out.println("[CLIENT | "+ new Date()+ "] High school table updated with " + highSchoolLessonPlans.size() + " lesson plans.");
        }

        if (collegeLessonPlans == null || collegeLessonPlans.isEmpty()) {
            System.out.println("[CLIENT | "+ new Date()+ "] No college lesson plans to display.");
        } else {
            allCollegeLessonPlans.setAll(collegeLessonPlans);
            collegeTableView.setItems(allCollegeLessonPlans);
            collegeTableView.refresh();
            System.out.println("[CLIENT | "+ new Date()+ "] College table updated with " + collegeLessonPlans.size() + " lesson plans.");
        }
    }

    @FXML
    private void handleRefresh() {
        if (controller != null) {
            controller.refreshTable();
        }
    }

    public void initializeSearchListener() {
        searchLessonPlanTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchLessonPlans(newValue);
        });
    }

    private void searchLessonPlans(String searchText) {
        ObservableList<LessonPlan> filteredHighSchool = FXCollections.observableArrayList();
        ObservableList<LessonPlan> filteredCollege = FXCollections.observableArrayList();

        for (LessonPlan lessonPlan : allHighSchoolLessonPlans) {
            if (lessonPlan.getObjectives().toLowerCase().contains(searchText.toLowerCase()) ||
                    lessonPlan.getTopicsCovered().toLowerCase().contains(searchText.toLowerCase())) {
                filteredHighSchool.add(lessonPlan);
            }
        }

        for (LessonPlan lessonPlan : allCollegeLessonPlans) {
            if (lessonPlan.getObjectives().toLowerCase().contains(searchText.toLowerCase()) ||
                    lessonPlan.getTopicsCovered().toLowerCase().contains(searchText.toLowerCase())) {
                filteredCollege.add(lessonPlan);
            }
        }

        highSchoolTableView.setItems(filteredHighSchool);
        collegeTableView.setItems(filteredCollege);
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