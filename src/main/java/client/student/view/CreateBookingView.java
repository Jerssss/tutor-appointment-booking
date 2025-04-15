package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import shared.classes.TutorSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

public class CreateBookingView {
    @FXML private TableColumn<TutorSession, String> academicLevelColumn;
    @FXML private VBox centerPane;
    @FXML private TableView<TutorSession> createReservationTableView;
    @FXML private TableColumn<TutorSession, String> dateColumn;
    @FXML private TableColumn<TutorSession, String> durationColumn;
    @FXML private Button refreshButton;
    @FXML private TableColumn<TutorSession, Void> reserveColumn;
    @FXML private TextField searchStudResTextField;
    @FXML private Label studResTitleLabel;
    @FXML private TableColumn<TutorSession, String> subjectColumn;
    @FXML private TableColumn<TutorSession, String> timeColumn;

    // Initialize method to set up table columns
    public void initialize() {
        // Set up cell value factories
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionDate().toLocalDate().toString()));
        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionTime()));
        durationColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSessionDuration() + " mins"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjectID"));

        // Add reserve button to each row
        reserveColumn.setCellFactory(param -> new TableCell<>() {
            private final Button reserveButton = new Button("Reserve");

            {
                reserveButton.setOnAction(event -> {
                    TutorSession session = getTableView().getItems().get(getIndex());
                    // Handle reservation logic here
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(reserveButton);
                }
            }
        });
    }

    // Animation methods
    public void refreshButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    public void refreshButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), refreshButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.play();
    }

    // Getter methods
    public TableView<TutorSession> getCreateReservationTableView() {
        return createReservationTableView;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public TextField getSearchStudResTextField() {
        return searchStudResTextField;
    }
}