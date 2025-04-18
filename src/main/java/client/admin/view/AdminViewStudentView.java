package client.admin.view;

import client.admin.controller.AdminViewStudentController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.Student;

import java.io.IOException;
import java.util.List;


public class AdminViewStudentView {

    @FXML
    private TextField searchResTextField;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addStudentButton;
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> studentIdColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, String> phoneNoColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableColumn<Student, String> academicLevelColumn;
    @FXML
    private TableColumn<Student, String> balanceColumn;
    @FXML
    private TableColumn<Student, String> optionColumn;

    private AdminViewStudentController controller;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT] Table columns initialized successfully.");
        initializeController();

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.searchStudents(newValue);
        });
        refreshButton.setOnAction(event -> controller.loadStudents());
        //setActionRefreshButton(event -> controller.loadStudents());
        addStudentButton.setOnAction(event -> openAddStudentWindow());

    }

    public void initializeTableColumns() {
        studentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserID()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        phoneNoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        academicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
        balanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getBalance())));
        optionColumn.setCellFactory(column -> createModifyButtonCellFactory());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing ModifyTerminalStatusController...");
        this.controller = new AdminViewStudentController(this);
        System.out.println("[CLIENT] ModifyTerminalStatusController successfully created.");
    }

    public TableCell<Student, String> createModifyButtonCellFactory(){
        return new TableCell<Student, String>() {
            private final Button modifyButton = new Button("Change Pass");

            {
                modifyButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                modifyButton.setOnAction(event -> {
                    Student student = getTableRow().getItem();
                    if (student != null) {
                        showModifyPane(student);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        };
    }

    public void showModifyPane(Student s) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_new_student_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Student");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Student window.");
        }
    }

    public void setActionRefreshButton(EventHandler<ActionEvent> event) {
        refreshButton.setOnAction(event);
    }

    private void openAddStudentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_new_student_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Student");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Student window.");
        }
    }


    public void updateTable(List<Student> data) {
        studentData.setAll(data); // Update dataset
        studentTableView.setItems(null); // Force reset
        studentTableView.setItems(studentData); // Reload table data
        studentTableView.refresh(); // Force UI refresh
        System.out.println("[CLIENT(Admin)] Student data updated. New table size: " + studentData.size());
    }
    private void applyHoverEffect(Button button) {
        button.setOnMouseEntered(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(0.9);
            st.setToY(0.9);
            st.setCycleCount(1);
            st.setAutoReverse(false);
            st.play();
        });

        button.setOnMouseExited(event -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.setCycleCount(1);
            st.setAutoReverse(false);
            st.play();
        });
    }

    public void addStudentButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addStudentButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addStudentButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addStudentButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
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
