package client.tutor.view;

import client.admin.controller.AdminViewStudentController;
import client.admin.view.AdminModifyStudentPopUp;
import client.tutor.controller.TutorViewLessonPlanController;
import client.tutor.controller.TutorViewStudentListPopUpController;
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
import shared.classes.LessonPlan;
import shared.classes.Student;

import java.io.IOException;
import java.util.List;

public class TutorViewLessonPlanView {
    @FXML private TableView<LessonPlan> lessonPlanListTableView;
    @FXML private TableColumn<LessonPlan, String> courseColumn;
    @FXML private TableColumn<LessonPlan, String> subjectColumn;
    @FXML private TableColumn<LessonPlan, String> objectivesColumn;
    @FXML private TableColumn<LessonPlan, String> topicsColumn;
    @FXML private TableColumn<LessonPlan, String> updateColumn;
    @FXML private TableColumn<LessonPlan, String> deleteColumn;
    @FXML private Button addLessonPlanButton;
    @FXML private Button refreshButton;
    @FXML private TextField searchStudResTextField;
    @FXML private Button updateLessonPlanButton;
    private TutorViewLessonPlanController controller;
    private ObservableList<LessonPlan> lessonPlanData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT] Table columns initialized successfully.");
        initializeController();
        addLessonPlanButton.setOnAction(event -> openAddLessonPlanWindow());
        try {
            searchStudResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchLessonPlan(newValue);
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        refreshButton.setOnAction(event -> controller.loadLessonPlan());
    }

    public void initializeTableColumns() {
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        objectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        topicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTopicsCovered())));
        updateColumn.setCellFactory(column -> createUpdateButtonCellFactory());
        deleteColumn.setCellFactory(column -> createDeleteButtonCellFactory());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing TutorViewLessonPlanController...");
        this.controller = new TutorViewLessonPlanController(this);
        System.out.println("[CLIENT] TutorViewLessonPlanController successfully created.");
    }

    public TableCell<LessonPlan, String> createUpdateButtonCellFactory(){
        return new TableCell<LessonPlan, String>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                updateButton.setOnAction(event -> {
                    LessonPlan lessonPLan = getTableRow().getItem();
                    if (lessonPLan != null) {
                        showUpdatePane(lessonPLan);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        };
    }

    public void showUpdatePane(LessonPlan lessonPlan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/modify_lesson_plan_window.fxml"));
            Parent root = loader.load();

            TutorModifyLessonPlanPopUp view = loader.getController();
            view.setLessonPlan(lessonPlan);

            Stage stage = new Stage();
            stage.setTitle("Update Lesson Plan");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Update Lesson plan window.");
        }
    }

    public TableCell<LessonPlan, String> createDeleteButtonCellFactory(){
        return new TableCell<LessonPlan, String>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    LessonPlan lessonPlan = getTableRow().getItem();
                    if (lessonPlan != null) {
                        boolean confirmed = showConfirmationDialog("Are you sure you want to delete this lesson plan?");
                        if (confirmed) {
                            controller.deleteLessonPlan(lessonPlan);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        };
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }

    private void openAddLessonPlanWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/add_new_lesson_plan_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Lesson Plan");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Lesson Plan window.");
        }
    }

    public void updateTable(List<LessonPlan> data) {
        lessonPlanData.setAll(data); // Update dataset
        lessonPlanListTableView.setItems(null); // Force reset
        lessonPlanListTableView.setItems(lessonPlanData); // Reload table data
        lessonPlanListTableView.refresh(); // Force UI refresh
    }

    public void addLessonPlanButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addLessonPlanButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
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
