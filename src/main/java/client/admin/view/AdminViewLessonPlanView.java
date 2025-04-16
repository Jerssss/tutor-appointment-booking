package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import shared.classes.LessonPlan;


public class AdminViewLessonPlanView {
    private final TableView<LessonPlan> highSchoolTableView;
    private final TableColumn<LessonPlan, String> highSchoolLessonPlanIDColumn;
    private final TableColumn<LessonPlan, String> highSchoolSubjectIDColumn;
//    private final TableColumn<LessonPlan, String> highSchoolSubjectNameColumn;
    private final TableColumn<LessonPlan, String> highSchoolObjectivesColumn;
    private final TableColumn<LessonPlan, String> highSchoolTopicsColumn;
    private final TableView<LessonPlan> collegeTableView;
    private final TableColumn<LessonPlan, String> collegeLessonPlanIDColumn;
    private final TableColumn<LessonPlan, String> collegeSubjectIDColumn;
//    private final TableColumn<LessonPlan, String> collegeSubjectNameColumn;
    private final TableColumn<LessonPlan, String> collegeObjectivesColumn;
    private final TableColumn<LessonPlan, String> collegeTopicsColumn;


    public AdminViewLessonPlanView(TableView<LessonPlan> highSchoolTableView, TableColumn<LessonPlan, String> highSchoolLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> highSchoolSubjectIDColumn,
                                   TableColumn<LessonPlan, String> highSchoolObjectivesColumn, TableColumn<LessonPlan, String> highSchoolTopicsColumn,
                                   TableView<LessonPlan> collegeTableView, TableColumn<LessonPlan, String> collegeLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> collegeSubjectIDColumn,
                                   TableColumn<LessonPlan, String> collegeObjectivesColumn, TableColumn<LessonPlan, String> collegeTopicsColumn) {
        this.highSchoolTableView = highSchoolTableView;
        this.highSchoolLessonPlanIDColumn = highSchoolLessonPlanIDColumn;
        this.highSchoolSubjectIDColumn = highSchoolSubjectIDColumn;
//        this.highSchoolSubjectNameColumn = highSchoolSubjectNameColumn;
        this.highSchoolObjectivesColumn = highSchoolObjectivesColumn;
        this.highSchoolTopicsColumn = highSchoolTopicsColumn;
        this.collegeTableView = collegeTableView;
        this.collegeLessonPlanIDColumn = collegeLessonPlanIDColumn;
        this.collegeSubjectIDColumn = collegeSubjectIDColumn;
//        this.collegeSubjectNameColumn = collegeSubjectNameColumn;
        this.collegeObjectivesColumn = collegeObjectivesColumn;
        this.collegeTopicsColumn = collegeTopicsColumn;

        setupTableColumns();
    }

    public void setupTableColumns() {
        highSchoolLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        highSchoolSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
//        highSchoolSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        highSchoolObjectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        highSchoolTopicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopicsCovered()));
        collegeLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        collegeSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
//        collegeSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        collegeObjectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        collegeTopicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTopicsCovered()));
    }

    public void displayLessonPlan(ObservableList<LessonPlan> allCollegeLessonPlansData, ObservableList<LessonPlan> allHighSchoolLessonPlansData) {
        collegeTableView.setItems(allCollegeLessonPlansData);
        highSchoolTableView.setItems(allHighSchoolLessonPlansData);
    }

//    public void displayLessonPlan(ObservableList<LessonPlan> allCollegeLessonPlansData, ObservableList<String> allCollegeSubjectNamesData, ObservableList<LessonPlan> allHighSchoolLessonPlansData, ObservableList<String> allHighSchoolSubjectNamesData) {
//        collegeTableView.setItems(allCollegeLessonPlansData);
//        collegeTableView.setItems(allCollegeSubjectNamesData);
//        highSchoolTableView.setItems(subjects);
//    }
}
