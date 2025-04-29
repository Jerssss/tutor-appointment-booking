package client.student.view;

import client.student.controller.ViewSubjectController;
import client.student.model.ViewSubjectModel;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import server.services.StudentServiceImpl;
import shared.classes.Subject;
import shared.interfaces.StudentService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewSubjectView implements Initializable {
    @FXML private TextField searchBookingTextField;
    @FXML private Button refreshButton;
    @FXML private TableView<Subject> viewBookingTableView;
    @FXML private TableColumn<Subject, String> courseColumn;
    @FXML private TableColumn<Subject, String> subjectColumn;
    @FXML private TableColumn<Subject, String> descriptionColumn;
    @FXML private TableColumn<Subject, String> academicLevelColumn;

    private final ObservableList<Subject> allSubjects = FXCollections.observableArrayList();
    private ViewSubjectController controller;

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
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectDescription()));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
    }

    private void initializeController() throws RemoteException {
        System.out.println("[CLIENT] Controller initialized!");
        StudentService service = new StudentServiceImpl(); // Initialize the service
        ViewSubjectModel model = new ViewSubjectModel(service);
        this.controller = new ViewSubjectController(this, model);

        // Call refreshTable to fetch and display data
        controller.refreshTable();
    }

    public void initializeSearchListener() {
        searchBookingTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSubjects(newValue.toLowerCase().trim());
        });
    }

    public void searchSubjects(String query) {
        String searchText = searchBookingTextField.getText().trim().toLowerCase();
        if (allSubjects.isEmpty()) {
            return;
        }

        if (query == null || query.isEmpty()) {
            viewBookingTableView.setItems(allSubjects);
            return;
        }

        List<Subject> filteredList = allSubjects.stream()
                .filter(subject -> subject.getSubjectID().toLowerCase().contains(searchText) ||
                        subject.getSubjectName().toLowerCase().contains(searchText) ||
                        subject.getSubjectDescription().toLowerCase().contains(searchText) ||
                        subject.getAcademicLevel().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        viewBookingTableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    public void updateTable(List<Subject> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            System.out.println("[CLIENT] No data to display in TableView.");
            return;
        }

        allSubjects.setAll(subjects);
        viewBookingTableView.setItems(allSubjects);
        viewBookingTableView.refresh();
        System.out.println("[CLIENT] Table updated with " + subjects.size() + " subjects.");
        viewBookingTableView.requestLayout();
    }

    @FXML
    private void handleRefresh() {
        System.out.println("[CLIENT] Refresh button clicked.");
        if (controller != null) {
            controller.refreshTable();
        }
    }

    public void setRefreshButtonAction(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
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