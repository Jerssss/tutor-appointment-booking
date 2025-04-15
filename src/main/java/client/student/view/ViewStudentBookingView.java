package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class ViewStudentBookingView {

    @FXML
    private VBox centerPane;

    @FXML
    private TableColumn<?, ?> courseColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> durationColumn;

    @FXML
    private TableColumn<?, ?> modeColumn;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchResTextField;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private TableColumn<?, ?> tutorColumn;

    @FXML
    private Label viewResLabel;

    @FXML
    private TableView<?> viewResTableView;


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
