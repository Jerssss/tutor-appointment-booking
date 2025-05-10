package client.landingpage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class StudentTutorIPPickerController {
    @FXML
    private ComboBox<String> ipComboBox;
    @FXML
    private TextField customIPField;
    @FXML
    private Button confirmButton;

    private String selectedIP;

    @FXML
    private void initialize() {
        // Initialize ComboBox with predefined IPs
        ipComboBox.getItems().addAll("localhost", "192.168.1.100", "192.168.191.231");
        ipComboBox.setValue("localhost");

        // Disable ComboBox when TextField is not empty
        customIPField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ipComboBox.setDisable(!newValue.trim().isEmpty());
            }
        });
    }

    @FXML
    private void handleConfirm() {
        String customIP = customIPField.getText().trim();
        // Prioritize TextField if not empty; otherwise, use ComboBox
        selectedIP = customIP.isEmpty() ? ipComboBox.getValue() : customIP;
        customIPField.getScene().getWindow().hide();
    }

    public String getSelectedIP() {
        return selectedIP;
    }
}