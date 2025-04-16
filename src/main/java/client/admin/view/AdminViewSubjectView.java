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
    private final TableColumn<Subject, String> subjectDescColumn;
    private final TableColumn<Subject, String> academicLevelColumn;

    private final Button addSubjectButton;


    public AdminViewSubjectView(TableView<Subject> viewResTableView, TableColumn<Subject, String> subjectIdColumn,
                                TableColumn<Subject, String> subjectNameColumn, TableColumn<Subject, String> subjectDescColumn,
                                TableColumn<Subject, String> academicLevelColumn, Button addSubjectButton) {
        this.viewResTableView = viewResTableView;
        this.subjectIdColumn = subjectIdColumn;
        this.subjectNameColumn = subjectNameColumn;
        this.subjectDescColumn = subjectDescColumn;
        this.academicLevelColumn = academicLevelColumn;
        this.addSubjectButton = addSubjectButton;

        setupTableColumns();
    }

    public void setupTableColumns() {
        subjectIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        subjectNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        subjectDescColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectDescription()));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectLevel()));
    }

    public void displaySubject(ObservableList<Subject> subjects) {
        viewResTableView.setItems(subjects);
    }
}
