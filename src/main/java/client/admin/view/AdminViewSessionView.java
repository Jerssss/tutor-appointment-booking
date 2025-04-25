package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import shared.classes.TutorSession;

import java.util.List;

public class AdminViewSessionView {
    private final TableView<TutorSession> tableView;
    private final TableColumn<TutorSession, String> sessionIDColumn;
    private final TableColumn<TutorSession, String> dateColumn;
    private final TableColumn<TutorSession, String> timeColumn;
    private final TableColumn<TutorSession, String> durationColumn;
    private final TableColumn<TutorSession, String> tutorIDColumn;
    private final TableColumn<TutorSession, String> subjectColumn;
    private final TableColumn<TutorSession, String> statusColumn;

    public AdminViewSessionView(
            TableView<TutorSession> tableView, TableColumn<TutorSession, String> sessionIDColumn,
            TableColumn<TutorSession, String> dateColumn,
            TableColumn<TutorSession, String> timeColumn,
            TableColumn<TutorSession, String> durationColumn,
            TableColumn<TutorSession, String> tutorIDColumn,
            TableColumn<TutorSession, String> subjectColumn, TableColumn<TutorSession, String> statusColumn) {

        this.tableView = tableView;
        this.sessionIDColumn = sessionIDColumn;
        this.dateColumn = dateColumn;
        this.timeColumn = timeColumn;
        this.durationColumn = durationColumn;
        this.tutorIDColumn = tutorIDColumn;
        this.subjectColumn = subjectColumn;
        this.statusColumn = statusColumn;

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
    }

    public void displaySession(ObservableList<TutorSession> sessions) {
        tableView.setItems(sessions);
    }


//    public TutorSession getListListedSession(){a
//        return tableView.getItems();
//    }
}
