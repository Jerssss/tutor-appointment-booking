package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import shared.classes.Subject;

import java.util.List;

public class AdminViewSubjectView {
    private final TableView<Subject> viewResTableView;
    private final TableColumn<Subject, String> subjectIdColumn;
    private final TableColumn<Subject, String> subjectNameColumn;
    private final TableColumn<Subject, String> academicLevelColumn;
    private final TableView<Subject> archivedResTableView;
    private final TableColumn<Subject, String> archivedSubjectIdColumn;
    private final TableColumn<Subject, String> archivedSubjectNameColumn;
    private final TableColumn<Subject, String> archivedAcademicLevelColumn;

    public AdminViewSubjectView(TableView<Subject> viewResTableView, TableColumn<Subject, String> subjectIdColumn,
                                TableColumn<Subject, String> subjectNameColumn,
                                TableColumn<Subject, String> academicLevelColumn, TableView<Subject> archivedResTableView, TableColumn<Subject, String> archivedSubjectIdColumn, TableColumn<Subject, String> archivedSubjectNameColumn, TableColumn<Subject, String> archivedAcademicLevelColumn) {
        this.viewResTableView = viewResTableView;
        this.subjectIdColumn = subjectIdColumn;
        this.subjectNameColumn = subjectNameColumn;
        this.academicLevelColumn = academicLevelColumn;
        this.archivedResTableView = archivedResTableView;
        this.archivedSubjectIdColumn = archivedSubjectIdColumn;
        this.archivedSubjectNameColumn = archivedSubjectNameColumn;
        this.archivedAcademicLevelColumn = archivedAcademicLevelColumn;

        setupTableColumns();
    }

    public void setupTableColumns() {
        subjectIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        subjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
        archivedSubjectIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        archivedSubjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        archivedAcademicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
    }

    public void displaySubject(ObservableList<Subject> subjects, ObservableList<Subject> archivedSubjects) {
        viewResTableView.setItems(subjects);
        archivedResTableView.setItems(archivedSubjects);
    }
}
