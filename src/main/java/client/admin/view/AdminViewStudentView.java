package client.admin.view;

import client.admin.controller.AdminViewStudentController;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Date;
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
    @FXML
    private TableColumn<Student, String> deleteColumn;
    @FXML
    private TableView<Student> archivedStudentTableView;
    @FXML
    private TableColumn<Student, String> arStudentIdColumn;
    @FXML
    private TableColumn<Student, String> arFirstNameColumn;
    @FXML
    private TableColumn<Student, String> arLastNameColumn;
    @FXML
    private TableColumn<Student, String> arPhoneNoColumn;
    @FXML
    private TableColumn<Student, String> arEmailColumn;
    @FXML
    private TableColumn<Student, String> arAcademicLevelColumn;
    @FXML
    private TableColumn<Student, String> arBalanceColumn;
    @FXML
    private TableColumn<Student, String> arDeleteColumn;

    private AdminViewStudentController controller;
    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<Student> archivedStudentData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Table columns initialized successfully.");
        initializeController();

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.searchStudents(newValue);
        });
        refreshButton.setOnAction(event -> {
            controller.loadStudents();
            controller.loadArchivedStudents();
        });

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
        deleteColumn.setCellFactory(column -> createDeleteButtonCellFactory());
        arStudentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserID()));
        arFirstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        arLastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        arPhoneNoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));
        arEmailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        arAcademicLevelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicLevel()));
        arBalanceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f", cellData.getValue().getBalance())));
        arDeleteColumn.setCellFactory(column -> createDeleteButtonCellFactory());
    }

    public void initializeController() {
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Initializing ModifyTerminalStatusController...");
        this.controller = new AdminViewStudentController(this);
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] ModifyTerminalStatusController successfully created.");
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
            System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Failed to load Add Student window.");
        }
    }

    public TableCell<Student, String> createModifyButtonCellFactory(){
        return new TableCell<Student, String>() {
            private final Button modifyButton = new Button("Change Pass");

            {
                modifyButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
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

    public void showModifyPane(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_student_window.fxml"));
            Parent root = loader.load();

            AdminModifyStudentPopUp view = loader.getController();
            view.setStudent(student);

            Stage stage = new Stage();
            stage.setTitle("Add Student");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Failed to load Add Student window.");
        }
    }

    private TableCell<Student, String> createDeleteButtonCellFactory() {
        return new TableCell<Student, String>() {
            private final Button deleteButton = new Button("Remove");

            {
                deleteButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
                deleteButton.setOnAction(event -> {
                    Student student = getTableRow().getItem();
                    if (student != null) {
                        showRemovePane(student);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        };
    }

    private void showRemovePane(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/delete_student.fxml"));
            Parent root = loader.load();

            AdminDeleteStudentPopUpView view = loader.getController();
            view.setStudent(student);
            view.setStudentController(this.controller);

            Stage stage = new Stage();
            stage.setTitle("Remove Student");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Failed to load Add Student window.");
        }
    }


    public void updateTable(List<Student> data) {
        studentData.setAll(data); // Update dataset
        studentTableView.setItems(null); // Force reset
        studentTableView.setItems(studentData); // Reload table data
        studentTableView.refresh(); // Force UI refresh
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Student data updated. New table size: " + studentData.size());
    }

    public void updateArchiveTable(List<Student> data) {
        archivedStudentData.setAll(data); // Update dataset
        archivedStudentTableView.setItems(null); // Force reset
        archivedStudentTableView.setItems(archivedStudentData); // Reload table data
        archivedStudentTableView.refresh(); // Force UI refresh
        System.out.println("[ADMIN CLIENT | "+ new Date()+ "] Archive student data updated. New table size: " + archivedStudentData.size());
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
