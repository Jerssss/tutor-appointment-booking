package client.tutor.view;

import client.tutor.controller.TutorViewLessonPlanController;
import client.tutor.model.TutorViewMoreLessonPlanPopUpModel;
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
import shared.classes.SessionManager;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class TutorViewLessonPlanView {
    @FXML private TableColumn<LessonPlan, String> viewMoreColumn;
    @FXML private TableView<LessonPlan> lessonPlanListTableView;
    @FXML private TableColumn<LessonPlan, String> courseColumn;
    @FXML private TableColumn<LessonPlan, String> subjectColumn;
    @FXML private TableColumn<LessonPlan, String> subjectIDColumn;
    @FXML private TableColumn<LessonPlan, String> objectivesColumn;
    @FXML private TableColumn<LessonPlan, String> topicsColumn;
    @FXML private TableColumn<LessonPlan, String> updateColumn;
    @FXML private TableColumn<LessonPlan, String> deleteColumn;
    @FXML private Button addLessonPlanButton;
    @FXML private Button refreshButton;
    @FXML private TextField searchStudResTextField;
    @FXML private Button updateLessonPlanButton;

    private TutorViewMoreLessonPlanPopUpModel model; // Model for the pop-up
    private TutorViewLessonPlanController controller;
    private ObservableList<LessonPlan> lessonPlanData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT | "+ new Date()+ "] Table columns initialized successfully.");

        String tutorID = getLoggedInTutorID(); // Replace with your method to get the logged-in tutor ID
        initializeController(tutorID); // Pass the tutor ID to the controller

        // Initialize the model
        this.model = new TutorViewMoreLessonPlanPopUpModel(); // Ensure the model is initialized

        addLessonPlanButton.setOnAction(event -> openAddLessonPlanWindow());
        try {
            searchStudResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.searchLessonPlan(newValue);
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        refreshButton.setOnAction(event -> controller.loadLessonPlans(tutorID)); // Pass the tutor ID to loadLessonPlans
    }

    public void initializeTableColumns() {
        courseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonPlanID()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        subjectIDColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectID()));
        objectivesColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectives()));
        topicsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTopicsCovered())));
        viewMoreColumn.setCellFactory(column -> createViewMoreButtonCellFactory());
        updateColumn.setCellFactory(column -> createUpdateButtonCellFactory());
        deleteColumn.setCellFactory(column -> createDeleteButtonCellFactory());
    }

    public void initializeController(String tutorID) {
        System.out.println("[CLIENT | "+ new Date()+ "] Initializing TutorViewLessonPlanController...");
        this.controller = new TutorViewLessonPlanController(this, tutorID); // Pass the tutor ID
        System.out.println("[CLIENT | "+ new Date()+ "] TutorViewLessonPlanController successfully created.");
    }

    public TableCell<LessonPlan, String> createUpdateButtonCellFactory(){
        return new TableCell<LessonPlan, String>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
                updateButton.setOnAction(event -> {
                    LessonPlan lessonPlan = getTableRow().getItem();
                    if (lessonPlan != null) {
                        showUpdatePane(lessonPlan);
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

    public TableCell<LessonPlan, String> createViewMoreButtonCellFactory() {
        return new TableCell<LessonPlan , String>() {
            private final Button viewButton = new Button("View More");

            {
                viewButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
                viewButton.setOnAction(event -> {
                    LessonPlan lessonPlan = getTableRow().getItem();
                    if (lessonPlan != null) {
                        lessonPlanListTableView.getSelectionModel().select(lessonPlan);
                        showViewMorePane(lessonPlan);
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        };
    }

    public void showViewMorePane(LessonPlan lessonPlan) {
        System.out.println("[CLIENT | "+ new Date()+ "] Selected session: " + lessonPlan);

        if (lessonPlan != null) {
            try {
                // Create a new instance of TutorViewMorePopUp using the constructor that accepts a model
                TutorViewMoreLessonPlanPopUp tutorViewMoreLessonPlanPopUp = new TutorViewMoreLessonPlanPopUp(model); // Pass the model here

                // Show the pop-up and pass the session ID
                tutorViewMoreLessonPlanPopUp.show(lessonPlan.getLessonPlanID());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[CLIENT | "+ new Date()+ "] Error displaying session details: " + e.getMessage());
            }
        } else {
            System.out.println("[CLIENT | "+ new Date()+ "] No session selected.");
        }
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
            System.out.println("[CLIENT | "+ new Date()+ "] Failed to load Update Lesson plan window.");
        }
    }

    public TableCell<LessonPlan, String> createDeleteButtonCellFactory(){
        return new TableCell<LessonPlan, String>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;-fx-background-radius: 15");
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
            System.out.println("[CLIENT | "+ new Date()+ "] Failed to load Add Lesson Plan window.");
        }
    }

    private String getLoggedInTutorID() {
        return SessionManager.getCurrentUserId(); // Use the SessionManager to get the current user ID
    }

    public void updateTable(List<LessonPlan> data) {
        lessonPlanData.setAll(data); // Update dataset
        lessonPlanListTableView.setItems(null); // Force reset
        lessonPlanListTableView.setItems(lessonPlanData); // Reload table data
        lessonPlanListTableView.refresh(); // Force UI refresh
    }

    public void addLessonPlanButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(1.0);
        st.setToY(1.0);
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