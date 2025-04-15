package client.admin.view;

import client.admin.controller.AdminViewSessionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.List;

// TEKA LANG HERE AHAHAHSGAHSASHSAVHBH

public class AdminViewSessionView {
    @FXML
    public TableView<List<String>> viewResTableView;

    @FXML
    public TableColumn<List<String>, String> dateColumn;
    @FXML
    public TableColumn<List<String>, String> timeColumn;
    @FXML
    public TableColumn<List<String>, String> durationColumn;
    @FXML
    public TableColumn<List<String>, String> academicLevelColumn;
    @FXML
    public TableColumn<List<String>, String> subjectColumn;

    @FXML
    private void initialize() {
        setupTableColumns();
    }


    public void setupTableColumns(){
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        durationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
    }

    public void displaySession(){
//        viewResTableView.setItems(AdminViewSessionController.sessionData);
    }
}
