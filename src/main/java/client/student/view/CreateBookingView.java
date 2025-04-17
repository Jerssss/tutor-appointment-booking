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
}