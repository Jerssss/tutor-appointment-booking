package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class AdminViewSessionView {
    private final TableView<List<String>> tableView;
    private final TableColumn<List<String>, String> sessionIDColumn;
    private final TableColumn<List<String>, String> dateColumn;
    private final TableColumn<List<String>, String> timeColumn;
    private final TableColumn<List<String>, String> durationColumn;
    private final TableColumn<List<String>, String> tutorIDColumn;
    private final TableColumn<List<String>, String> subjectColumn;
    private final TableColumn<List<String>, String> statusColumn;

    public AdminViewSessionView(
            TableView<List<String>> tableView, TableColumn<List<String>, String> sessionIDColumn,
            TableColumn<List<String>, String> dateColumn,
            TableColumn<List<String>, String> timeColumn,
            TableColumn<List<String>, String> durationColumn,
            TableColumn<List<String>, String> tutorIDColumn,
            TableColumn<List<String>, String> subjectColumn, TableColumn<List<String>, String> statusColumn) {

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
        sessionIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        tutorIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
    }

    public void displaySession(ObservableList<List<String>> sessions) {
        tableView.setItems(sessions);
    }


//    public List<String> getListListedSession(){a
//        return tableView.getItems();
//    }
}
