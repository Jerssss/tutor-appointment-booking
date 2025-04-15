package client.admin.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.List;

public class AdminViewSessionView {
    private final TableView<List<String>> tableView;
    private final TableColumn<List<String>, String> dateColumn;
    private final TableColumn<List<String>, String> timeColumn;
    private final TableColumn<List<String>, String> durationColumn;
    private final TableColumn<List<String>, String> academicLevelColumn;
    private final TableColumn<List<String>, String> subjectColumn;

    public AdminViewSessionView(
            TableView<List<String>> tableView,
            TableColumn<List<String>, String> dateColumn,
            TableColumn<List<String>, String> timeColumn,
            TableColumn<List<String>, String> durationColumn,
            TableColumn<List<String>, String> academicLevelColumn,
            TableColumn<List<String>, String> subjectColumn) {

        this.tableView = tableView;
        this.dateColumn = dateColumn;
        this.timeColumn = timeColumn;
        this.durationColumn = durationColumn;
        this.academicLevelColumn = academicLevelColumn;
        this.subjectColumn = subjectColumn;

        setupTableColumns();
    }

    public void setupTableColumns() {
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
    }

    public void displaySession(ObservableList<List<String>> sessions) {
        tableView.setItems(sessions);
    }
}
