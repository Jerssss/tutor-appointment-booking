package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import shared.classes.TutorSession;

import java.util.List;

public class AdminViewSessionView {
    private final TableView<TutorSession> viewResTableView;
    private final TableColumn<TutorSession, String> sessionIDColumn;
    private final TableColumn<TutorSession, String> dateColumn;
    private final TableColumn<TutorSession, String> timeColumn;
    private final TableColumn<TutorSession, String> durationColumn;
    private final TableColumn<TutorSession, String> tutorIDColumn;
    private final TableColumn<TutorSession, String> subjectColumn;
    private final TableColumn<TutorSession, String> statusColumn;
    private final TableView<TutorSession> archivedResTableView;
    private final TableColumn<TutorSession, String> archivedSessionIDColumn;
    private final TableColumn<TutorSession, String> archivedDateColumn;
    private final TableColumn<TutorSession, String> archivedTimeColumn;
    private final TableColumn<TutorSession, String> archivedDurationColumn;
    private final TableColumn<TutorSession, String> archivedTutorIDColumn;
    private final TableColumn<TutorSession, String> archivedSubjectColumn;
    private final TableColumn<TutorSession, String> archivedStatusColumn;
    private final TableColumn<TutorSession, Void> archivedViewMoreColumn;
    private final TableColumn<TutorSession, Void> archivedOptionColumn;
    private final TableColumn<TutorSession, Void> archivedDeleteColumn;

    public AdminViewSessionView(
            TableView<TutorSession> viewResTableView, TableColumn<TutorSession, String> sessionIDColumn,
            TableColumn<TutorSession, String> dateColumn,
            TableColumn<TutorSession, String> timeColumn,
            TableColumn<TutorSession, String> durationColumn,
            TableColumn<TutorSession, String> tutorIDColumn,
            TableColumn<TutorSession, String> subjectColumn, TableColumn<TutorSession, String> statusColumn, TableView<TutorSession> archivedResTableView, TableColumn<TutorSession, String> archivedSessionIDColumn, TableColumn<TutorSession, String> archivedDateColumn, TableColumn<TutorSession, String> archivedTimeColumn, TableColumn<TutorSession, String> archivedDurationColumn, TableColumn<TutorSession, String> archivedTutorIDColumn, TableColumn<TutorSession, String> archivedSubjectColumn, TableColumn<TutorSession, String> archivedStatusColumn, TableColumn<TutorSession, Void> archivedViewMoreColumn, TableColumn<TutorSession, Void> archivedOptionColumn, TableColumn<TutorSession, Void> archivedDeleteColumn) {

        this.viewResTableView = viewResTableView;
        this.sessionIDColumn = sessionIDColumn;
        this.dateColumn = dateColumn;
        this.timeColumn = timeColumn;
        this.durationColumn = durationColumn;
        this.tutorIDColumn = tutorIDColumn;
        this.subjectColumn = subjectColumn;
        this.statusColumn = statusColumn;
        this.archivedResTableView = archivedResTableView;
        this.archivedSessionIDColumn = archivedSessionIDColumn;
        this.archivedDateColumn = archivedDateColumn;
        this.archivedTimeColumn = archivedTimeColumn;
        this.archivedDurationColumn = archivedDurationColumn;
        this.archivedTutorIDColumn = archivedTutorIDColumn;
        this.archivedSubjectColumn = archivedSubjectColumn;
        this.archivedStatusColumn = archivedStatusColumn;
        this.archivedViewMoreColumn = archivedViewMoreColumn;
        this.archivedOptionColumn = archivedOptionColumn;
        this.archivedDeleteColumn = archivedDeleteColumn;

        setupTableColumns();
    }

    public void setupTableColumns() {
        sessionIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionID()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionDate().toString()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionTime().toString()));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));
        tutorIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTutorID()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionStatus()));
        archivedSessionIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionID()));
        archivedDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionDate().toString()));
        archivedTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionTime().toString()));
        archivedDurationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSessionDuration())));
        archivedTutorIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTutorID()));
        archivedSubjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        archivedStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSessionStatus()));
    }

    public void displaySession(ObservableList<TutorSession> sessions, ObservableList<TutorSession> archivedSessions) {
        viewResTableView.setItems(sessions);
        archivedResTableView.setItems(archivedSessions);
    }


//    public TutorSession getListListedSession(){a
//        return tableView.getItems();
//    }
}
