package client.admin.view;

import client.admin.controller.AdminViewTutorController;
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
import shared.classes.Tutor;

import java.io.IOException;
import java.util.List;

public class AdminViewTutorView {
    @FXML
    private TextField searchResTextField;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addTutorButton;
    @FXML
    private TableView<Tutor> tutorTableView;
    @FXML
    private TableColumn<Tutor, String> tutorIdColumn;
    @FXML
    private TableColumn<Tutor, String> firstNameColumn;
    @FXML
    private TableColumn<Tutor, String> lastNameColumn;
    @FXML
    private TableColumn<Tutor, String> phoneNoColumn;
    @FXML
    private TableColumn<Tutor, String> emailColumn;
    @FXML
    private TableColumn<Tutor, String> expertiseColumn;
    @FXML
    private TableColumn<Tutor, String> optionColumn;
    private AdminViewTutorController controller;
    private ObservableList<Tutor> tutorData = FXCollections.observableArrayList();

    public void initialize() {
        initializeTableColumns();
        System.out.println("[CLIENT] Table columns initialized successfully.");
        initializeController();

        searchResTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.searchTutors(newValue);
        });
        refreshButton.setOnAction(event -> controller.loadTutors());
        addTutorButton.setOnAction(event -> openAddTutorWindow());
    }

    public void initializeTableColumns() {
        tutorIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserID()));
        firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        phoneNoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getPhoneNumber())));
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        expertiseColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpertise()));
        optionColumn.setCellFactory(column -> createModifyButtonCellFactory());
    }

    public void initializeController() {
        System.out.println("[CLIENT] Initializing ModifyTerminalStatusController...");
        this.controller = new AdminViewTutorController(this);
        System.out.println("[CLIENT] ModifyTerminalStatusController successfully created.");
    }

    public TableCell<Tutor, String> createModifyButtonCellFactory(){
        return new TableCell<Tutor, String>() {
            private final Button modifyButton = new Button("Change Pass");

            {
                modifyButton.setStyle("-fx-background-color: #6F2E2E; -fx-text-fill: white;");
                modifyButton.setOnAction(event -> {
                    Tutor tutor = getTableRow().getItem();
                    if (tutor != null) {
                        showModifyPane(tutor);
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

    public void showModifyPane(Tutor tutor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_tutor_window.fxml"));
            Parent root = loader.load();

            AdminModifyTutorPopUp view = loader.getController();
            view.setTutor(tutor);

            Stage stage = new Stage();
            stage.setTitle("Modify Tutor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Tutor window.");
        }
    }

    private void openAddTutorWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_new_tutor_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Tutor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
            stage.showAndWait(); // Waits for the window to be closed before resuming

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[CLIENT] Failed to load Add Tutor window.");
        }
    }


    public void updateTable(List<Tutor> data) {
        tutorData.setAll(data); // Update dataset
        tutorTableView.setItems(null); // Force reset
        tutorTableView.setItems(tutorData); // Reload table data
        tutorTableView.refresh(); // Force UI refresh
        System.out.println("[CLIENT(Admin)] Tutor data updated. New table size: " + tutorData.size());
    }

    public void addTutorButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addTutorButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void addTutorButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), addTutorButton);
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
