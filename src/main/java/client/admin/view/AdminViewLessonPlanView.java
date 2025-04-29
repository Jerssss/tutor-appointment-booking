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
    private final TableColumn<LessonPlan, String> highSchoolSubjectNameColumn;

    private final TableView<LessonPlan> highSchoolArchivedTableView;
    private final TableColumn<LessonPlan, String> highSchoolArchivedLessonPlanIDColumn;
    private final TableColumn<LessonPlan, String> highSchoolArchivedSubjectIDColumn;
    private final TableColumn<LessonPlan, String> highSchoolArchivedSubjectNameColumn;

    private final TableView<LessonPlan> collegeTableView;
    private final TableColumn<LessonPlan, String> collegeLessonPlanIDColumn;
    private final TableColumn<LessonPlan, String> collegeSubjectIDColumn;
    private final TableColumn<LessonPlan, String> collegeSubjectNameColumn;

    private final TableView<LessonPlan> collegeArchivedTableView;
    private final TableColumn<LessonPlan, String> collegeArchivedLessonPlanIDColumn;
    private final TableColumn<LessonPlan, String> collegeArchivedSubjectIDColumn;
    private final TableColumn<LessonPlan, String> collegeArchivedSubjectNameColumn;


    public AdminViewLessonPlanView(TableView<LessonPlan> highSchoolTableView, TableColumn<LessonPlan, String> highSchoolLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> highSchoolSubjectIDColumn, TableColumn<LessonPlan, String> highSchoolSubjectNameColumn,
                                   TableView<LessonPlan> collegeTableView, TableColumn<LessonPlan, String> collegeLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> collegeSubjectIDColumn, TableColumn<LessonPlan, String> collegeSubjectNameColumn,
                                   TableView<LessonPlan> highSchoolArchivedTableView, TableColumn<LessonPlan, String> highSchoolArchivedLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> highSchoolArchivedSubjectIDColumn, TableColumn<LessonPlan, String> highSchoolArchivedSubjectNameColumn,
                                   TableView<LessonPlan> collegeArchivedTableView, TableColumn<LessonPlan, String> collegeArchivedLessonPlanIDColumn,
                                   TableColumn<LessonPlan, String> collegeArchivedSubjectIDColumn, TableColumn<LessonPlan, String> collegeArchivedSubjectNameColumn) {
        this.highSchoolTableView = highSchoolTableView;
        this.highSchoolLessonPlanIDColumn = highSchoolLessonPlanIDColumn;
        this.highSchoolSubjectIDColumn = highSchoolSubjectIDColumn;
        this.highSchoolSubjectNameColumn = highSchoolSubjectNameColumn;
        this.highSchoolArchivedTableView = highSchoolArchivedTableView;
        this.highSchoolArchivedLessonPlanIDColumn = highSchoolArchivedLessonPlanIDColumn;
        this.highSchoolArchivedSubjectIDColumn = highSchoolArchivedSubjectIDColumn;
        this.highSchoolArchivedSubjectNameColumn = highSchoolArchivedSubjectNameColumn;
        this.collegeTableView = collegeTableView;
        this.collegeLessonPlanIDColumn = collegeLessonPlanIDColumn;
        this.collegeSubjectIDColumn = collegeSubjectIDColumn;
        this.collegeSubjectNameColumn = collegeSubjectNameColumn;
        this.collegeArchivedTableView = collegeArchivedTableView;
        this.collegeArchivedLessonPlanIDColumn = collegeArchivedLessonPlanIDColumn;
        this.collegeArchivedSubjectIDColumn = collegeArchivedSubjectIDColumn;
        this.collegeArchivedSubjectNameColumn = collegeArchivedSubjectNameColumn;
        setupTableColumns();
    }

    public void setupTableColumns() {
        highSchoolLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        highSchoolSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        highSchoolSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        highSchoolArchivedLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        highSchoolArchivedSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        highSchoolArchivedSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        collegeLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        collegeSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        collegeSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        collegeArchivedLessonPlanIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        collegeArchivedSubjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        collegeArchivedSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
    }

    public void displayLessonPlan(ObservableList<LessonPlan> allCollegeLessonPlansData, ObservableList<LessonPlan> allHighSchoolLessonPlansData,
                                  ObservableList<LessonPlan> allArchivedCollegeLessonPlansData, ObservableList<LessonPlan> allArchivedHighSchoolLessonPlansData) {
        collegeTableView.setItems(allCollegeLessonPlansData);
        highSchoolTableView.setItems(allHighSchoolLessonPlansData);
        collegeArchivedTableView.setItems(allArchivedCollegeLessonPlansData);
        highSchoolArchivedTableView.setItems(allArchivedHighSchoolLessonPlansData);
    }

}
