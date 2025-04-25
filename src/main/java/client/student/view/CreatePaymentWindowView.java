package client.student.view;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class CreatePaymentWindowView {
    @FXML
    private Label acadLevelLabel;

    @FXML
    private ToggleGroup bankGroup;

    @FXML
    private RadioButton bdoRadio;

    @FXML
    private Button cancelButton;

    @FXML
    private Button payButton;

    @FXML
    private Label dateLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private RadioButton gcashRadio;

    @FXML
    private Label modeLabel;

    @FXML
    private VBox paymentOptionsVBox;

    @FXML
    private Label priceLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private RadioButton unionBankRadio;

    public void payButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), payButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

    public void payButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), payButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }

}
