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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import shared.classes.LessonPlan;
import shared.classes.SessionManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    // Archived Tab
    @FXML private TableView<LessonPlan> archivedLessonPlanListTableView;
    @FXML private TableColumn<LessonPlan, String> archivedCourseColumn;
    @FXML private TableColumn<LessonPlan, String> archivedSubjectColumn;
    @FXML private TableColumn<LessonPlan, String> archivedSubjectIDColumn;
    @FXML private TableColumn<LessonPlan, String> archivedObjectivesColumn;
    @FXML private TableColumn<LessonPlan, String> archivedTopicsColumn;
    @FXML private TableColumn<LessonPlan, String> archivedViewMoreColumn;
    @FXML private TableColumn<LessonPlan, String> archivedUpdateColumn;
    @FXML private TableColumn<LessonPlan, String> archivedDeleteColumn;

    @FXML private Button addLessonPlanButton;
    @FXML private Button refreshButton;
    @FXML private TextField searchStudResTextField;

    private TutorViewMoreLessonPlanPopUpModel model; // Model for the pop-up
    private TutorViewLessonPlanController controller;
    private ObservableList<LessonPlan> lessonPlanData = FXCollections.observableArrayList();
    private ObservableList<LessonPlan> archivedLessonPlanData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        initializeArchivedTableColumns();
        System.out.println("[CLIENT] Table columns initialized successfully.");

        String tutorID = getLoggedInTutorID();
        initializeController(tutorID);

        this.model = new TutorViewMoreLessonPlanPopUpModel();

        addLessonPlanButton.setOnAction(event -> openAddLessonPlanWindow());
        try {
            searchStudResTextField.textProperty().addListener((obs, old, nw) -> {
                controller.searchLessonPlan(nw);
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        refreshButton.setOnAction(event -> controller.loadLessonPlans(tutorID));
    }

    public void initializeTableColumns() {
        courseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLessonPlanID()));
        subjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectName()));
        subjectIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectID()));
        objectivesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObjectives()));
        topicsColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTopicsCovered())));
        viewMoreColumn.setCellFactory(col -> createViewMoreButtonCellFactory());
        updateColumn.setCellFactory(col -> createUpdateButtonCellFactory());
        deleteColumn.setCellFactory(col -> createDeleteButtonCellFactory());
    }

    public void initializeArchivedTableColumns() {
        archivedCourseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLessonPlanID()));
        archivedSubjectColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectName()));
        archivedSubjectIDColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectID()));
        archivedObjectivesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObjectives()));
        archivedTopicsColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getTopicsCovered())));
        archivedViewMoreColumn.setCellFactory(col -> createArchivedViewMoreButtonCellFactory());
        archivedUpdateColumn.setVisible(false);
        archivedDeleteColumn.setVisible(false);
    }

    private TableCell<LessonPlan, String> createArchivedViewMoreButtonCellFactory() {
        return new TableCell<LessonPlan, String>() {
            private final Button viewButton = new Button();
            {
                // load the same ViewMoreIcon.png
                Image img = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                viewButton.setGraphic(iv);

                // match your existing styling
                viewButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );

                viewButton.setOnAction(event -> {
                    LessonPlan lp = getTableRow().getItem();
                    if (lp != null) {
                        // select and open the same detail pane
                        archivedLessonPlanListTableView.getSelectionModel().select(lp);
                        showViewMorePane(lp);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        };
    }


    public void initializeController(String tutorID) {
        System.out.println("[CLIENT] Initializing TutorViewLessonPlanController...");
        this.controller = new TutorViewLessonPlanController(this, tutorID);
        System.out.println("[CLIENT] TutorViewLessonPlanController successfully created.");
    }

    public TableCell<LessonPlan, String> createViewMoreButtonCellFactory() {
        return new TableCell<LessonPlan, String>() {
            private final Button viewButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/ViewMoreIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                viewButton.setGraphic(iv);
                viewButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                viewButton.setOnAction(event -> {
                    LessonPlan lp = getTableRow().getItem();
                    if (lp != null) showViewMorePane(lp);
                });
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewButton);
            }
        };
    }

    public TableCell<LessonPlan, String> createUpdateButtonCellFactory() {
        return new TableCell<LessonPlan, String>() {
            private final Button updateButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/EditIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                updateButton.setGraphic(iv);
                updateButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                updateButton.setOnAction(event -> {
                    LessonPlan lp = getTableRow().getItem();
                    if (lp != null) showUpdatePane(lp);
                });
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : updateButton);
            }
        };
    }

    public TableCell<LessonPlan, String> createDeleteButtonCellFactory() {
        return new TableCell<LessonPlan, String>() {
            private final Button deleteButton = new Button();
            {
                Image img = new Image(getClass().getResourceAsStream("/images/client/DeleteIcon.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(16);
                iv.setFitHeight(16);
                deleteButton.setGraphic(iv);
                deleteButton.setStyle(
                        "-fx-background-color: #6F2E2E; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                );
                deleteButton.setOnAction(event -> {
                    LessonPlan lp = getTableRow().getItem();
                    if (lp != null && showConfirmationDialog("Are you sure you want to delete this lesson plan?")) {
                        controller.deleteLessonPlan(lp);
                    }
                });
            }
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        };
    }

    public void showViewMorePane(LessonPlan lessonPlan) {
        System.out.println("[CLIENT] Selected lesson plan: " + lessonPlan);
        if (lessonPlan != null) {
            try {
                TutorViewMoreLessonPlanPopUp popUp = new TutorViewMoreLessonPlanPopUp(model);
                popUp.show(lessonPlan.getLessonPlanID());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("[CLIENT] Error displaying lesson plan details: " + e.getMessage());
            }
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load UpdateLessonPlan window.");
        }
    }

    private void openAddLessonPlanWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tutor/add_new_lesson_plan_window.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Lesson Plan");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Lesson Plan window.");
        }
    }

    private String getLoggedInTutorID() {
        return SessionManager.getCurrentUserId();
    }

    public void updateTable(List<LessonPlan> data) {
        ObservableList<LessonPlan> unique = FXCollections.observableArrayList();
        for (LessonPlan lp : data) {
            boolean dup = unique.stream()
                    .anyMatch(e -> e.getLessonPlanID().equals(lp.getLessonPlanID()));
            if (!dup) unique.add(lp);
        }
        lessonPlanData.setAll(unique);
        lessonPlanListTableView.setItems(lessonPlanData);
        lessonPlanListTableView.refresh();
    }

    public void updateArchivedTable(List<LessonPlan> archivedData) {
        archivedLessonPlanData.setAll(removeDuplicates(archivedData));
        archivedLessonPlanListTableView.setItems(archivedLessonPlanData);
        archivedLessonPlanListTableView.refresh();
    }

    private List<LessonPlan> removeDuplicates(List<LessonPlan> data) {
        return data.stream().distinct().collect(Collectors.toList());
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.OK;
    }



    // Scale transition methods
    public void addLessonPlanButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(1.0); st.setToY(1.0); st.play();
    }
    public void addLessonPlanButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addLessonPlanButton);
        st.setToX(0.9); st.setToY(0.9); st.play();
    }
    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0); st.setToY(1.0); st.play();
    }
    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9); st.setToY(0.9); st.play();
    }
}
