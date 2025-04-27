package client.admin.controller;

import client.admin.model.AdminModifySessionPopUpModel;
import client.admin.view.AdminModifySessionPopUpView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import server.services.AdminServiceImpl;
import shared.interfaces.AdminService;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminModifySessionPopUpController implements Initializable {
    private final AdminModifySessionPopUpModel model;
    private AdminModifySessionPopUpView view;
    @FXML
    private ComboBox<String> sessionVisibilityComboBox;
    @FXML
    private ComboBox<String> sessionStatusComboBox;
    @FXML
    private ComboBox<String> sessionTypeComboBox;
    @FXML
    private ComboBox<String> sessionModeComboBox;
    @FXML
    private Button modifySessionButton;
    @FXML
    private TextField numberOfStudentsField;
    @FXML
    private TextField sessionPriceField;
    @FXML
    private TextField maxNumberOfStudentsField;
    @FXML
    private Label numberStudentsErrorLabel;
    @FXML
    private Label maxStudentsErrorLabel;
    @FXML
    private Label priceErrorLabel;

    public AdminModifySessionPopUpController() throws RemoteException {
        this.model = new AdminModifySessionPopUpModel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminModifySessionPopUpView(
                sessionVisibilityComboBox,
                sessionStatusComboBox,
                sessionTypeComboBox,
                sessionModeComboBox,
                modifySessionButton
        );

        try {
            initializeData();
            populateFields();
            setupEventHandlers();
            view.setupButtonHoverEffects();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sessionTypeComboBox.setOnAction(event -> {
            String selectedType = sessionTypeComboBox.getSelectionModel().getSelectedItem();
            if ("Individual".equalsIgnoreCase(selectedType)) {
                maxNumberOfStudentsField.setText("1");
                maxNumberOfStudentsField.setEditable(false);
            } else {
                maxNumberOfStudentsField.setEditable(true);
                maxNumberOfStudentsField.setPromptText("Enter maximum students...");
                maxNumberOfStudentsField.clear();
            }
        });

    }

    private void initializeData() throws RemoteException {
        view.initializeComboBoxes();
    }

    private void setupEventHandlers() {
        view.setModifySessionButton(this::handleUpdateSession);
    }


    private void handleUpdateSession(ActionEvent event) {
        numberStudentsErrorLabel.setVisible(false);
        maxStudentsErrorLabel.setVisible(false);
        priceErrorLabel.setVisible(false);

        boolean hasError = false;

        String sessionVisibility = view.getSelectedSessionVisibility();
        String sessionStatus = view.getSelectedSessionStatus();
        String sessionType = view.getSelectedSessionType();
        String sessionMode = view.getSelectedSessionMode();
        String numStudents = numberOfStudentsField.getText();
        String maxStudents = maxNumberOfStudentsField.getText();
        String price = sessionPriceField.getText();

        if (numStudents.isEmpty()) {
            numberStudentsErrorLabel.setText("This field is required.");
            numberStudentsErrorLabel.setVisible(true);
            hasError = true;
        } else if (!numStudents.matches("\\d+")) {
            numberStudentsErrorLabel.setText("Only numeric values allowed.");
            numberStudentsErrorLabel.setVisible(true);
            hasError = true;
        }

        if (maxStudents.isEmpty()) {
            maxStudentsErrorLabel.setText("This field is required.");
            maxStudentsErrorLabel.setVisible(true);
            hasError = true;
        } else if (!maxStudents.matches("\\d+")) {
            maxStudentsErrorLabel.setText("Only numeric values allowed.");
            maxStudentsErrorLabel.setVisible(true);
            hasError = true;
        }

        if (price.isEmpty()) {
            priceErrorLabel.setText("This field is required.");
            priceErrorLabel.setVisible(true);
            hasError = true;
        } else if (!price.matches("\\d+(\\.\\d{1,2})?")) {
            priceErrorLabel.setText("Enter valid amount (e.g., 50 or 50.00).");
            priceErrorLabel.setVisible(true);
            hasError = true;
        }

        if (!numStudents.isEmpty() && !maxStudents.isEmpty()
                && numStudents.matches("\\d+") && maxStudents.matches("\\d+")) {

            int num = Integer.parseInt(numStudents);
            int max = Integer.parseInt(maxStudents);

            if (num > max) {
                numberStudentsErrorLabel.setText("Cannot exceed maximum students.");
                numberStudentsErrorLabel.setVisible(true);
                hasError = true;
            }
        }

        if (hasError) return;

        try {
            String sessionID = AdminViewSessionController.getClickedSession().getSessionID();
            model.updateSession(sessionID, sessionMode, sessionType, numStudents, maxStudents, price, sessionStatus, sessionVisibility);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Session Updated");
            successAlert.setHeaderText(null);
            successAlert.setContentText("The session was successfully updated.");

            successAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    ((Stage) modifySessionButton.getScene().getWindow()).close();
                }
            });

        } catch (RemoteException | SQLException e) {
            e.printStackTrace();
        }

    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/modify_session.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Edit Session");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Edit Session window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFields() throws RemoteException, SQLException {
        String sessionID = AdminViewSessionController.getClickedSession().getSessionID();
        var details = model.getEditableDetails(sessionID);

        sessionVisibilityComboBox.setValue(details.getVisibility());
        sessionStatusComboBox.setValue(details.getSessionStatus());
        sessionTypeComboBox.setValue(details.getSessionType());
        sessionModeComboBox.setValue(details.getSessionMode());
        numberOfStudentsField.setText(String.valueOf(details.getNumberOfStudents()));
        maxNumberOfStudentsField.setText(String.valueOf(details.getMaximumStudents()));

        if("Cancelled".equalsIgnoreCase(details.getSessionStatus()) || "In Progress".equalsIgnoreCase(details.getSessionStatus()) || "Completed".equalsIgnoreCase(details.getSessionStatus())){
            sessionModeComboBox.setDisable(true);
            sessionTypeComboBox.setDisable(true);
            numberOfStudentsField.setEditable(false);
            maxNumberOfStudentsField.setEditable(false);
            sessionPriceField.setEditable(false);
        }

        if ("Individual".equalsIgnoreCase(details.getSessionType())) {
            maxNumberOfStudentsField.setEditable(false);
        }

        if (sessionPriceField != null) {
            sessionPriceField.setText(String.valueOf(details.getSessionPrice()));
        }
    }

    @FXML
    private void modifySessionButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void modifySessionButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}