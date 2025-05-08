package client.admin.controller;

import client.admin.model.AdminAddSessionPopUpModel;
import client.admin.view.AdminAddSessionPopUpView;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class AdminAddSessionPopUpController implements Initializable {
    private final AdminAddSessionPopUpModel model;
    private AdminAddSessionPopUpView view;
    private final AdminService service = new AdminServiceImpl();

    @FXML private ComboBox<String> tutorIDComboBox;
    @FXML private ComboBox<String> subjectNameComboBox;
    @FXML private ComboBox<String> sessionModeComboBox;
    @FXML private ComboBox<String> sessionTypeComboBox;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private ComboBox<String> tutorNameComboBox;
    @FXML private ComboBox<String> subjectIdComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button addSessionWindowButton;
    @FXML private TextField sessionPriceTextField;
    @FXML private TextField maxStudentsTextField;
    @FXML private Label maxStudentsErrorLabel;
    @FXML private Label sessionPriceErrorLabel;

    public AdminAddSessionPopUpController() throws RemoteException {
        this.model = new AdminAddSessionPopUpModel();
//        showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminAddSessionPopUpView(
                tutorIDComboBox,
                subjectNameComboBox,
                sessionModeComboBox,
                sessionTypeComboBox,
                startTimeComboBox,
                durationComboBox,
                tutorNameComboBox,
                subjectIdComboBox,
                datePicker,
                addSessionWindowButton,
                sessionPriceTextField,
                maxStudentsTextField
        );

        try {
            initializeData();
            setupEventHandlers();
            view.setupButtonHoverEffects();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(java.time.LocalDate.now()));
            }
        });



    }

    private void initializeData() throws RemoteException {
        List<String> tutorNames = model.getAllTutorNames();
//        List<String> subjectNames = model.getAllSubjectNames();
        List<String> tutorIDs = model.getAllTutorIDs();
//        List<String> subjectIDs = model.getAllSubjectIDs();
//        view.initializeComboBoxes(tutorNames, tutorIDs, subjectIDs, subjectNames);
        view.initializeComboBoxes(tutorNames, tutorIDs);
    }

    private void setupEventHandlers() {
        view.setTutorSelectionHandler(this::handleTutorSelection);
        view.setAddSessionButtonAction(this::handleAddSession);
        view.setDateSelectionHandler(this::handleDateSelection);
        view.setStartTimeSelectionHandler(this::handleStartTimeSelection);

        tutorIDComboBox.setOnAction(this::handleTutorIdSelection);
        tutorNameComboBox.setOnAction(this::handleTutorNameSelection);
        subjectNameComboBox.setOnAction(this::handleSubjectNameSelection);
        subjectIdComboBox.setOnAction(this::handleSubjectIdSelection);

        sessionTypeComboBox.setOnAction(event -> {
            String type = sessionTypeComboBox.getValue();
            if ("Individual".equalsIgnoreCase(type)) {
                maxStudentsTextField.setText("1");
                maxStudentsTextField.setEditable(false);
            } else {
                maxStudentsTextField.setText("");
                maxStudentsTextField.setEditable(true);
            }
        });

    }


    private void handleTutorSelection(ActionEvent event) {
            String tutor = view.getSelectedTutorName();
    }

    private void handleDateSelection(ActionEvent event) {
        try {
            String tutor = view.getSelectedTutorID();
            String date = view.getSelectedDate().toString();
            List<String> times = model.getAvailableTimeTutor(tutor, date);
            view.updateStartTimes(times);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    private void handleTutorIdSelection(ActionEvent event) {
        String tutorID = tutorIDComboBox.getValue();
        if (tutorID != null && !tutorID.isEmpty()) {
            try {
                String tutorName = model.getTutorName(tutorID);
                tutorNameComboBox.setValue(tutorName);
                List<String> subjectNames = model.getAllSubjectNames(tutorID);
                view.updateSubjectName(subjectNames);
                List<String> subjectID = model.getAllSubjectIDs(tutorID);
                view.updateSubjectID(subjectID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleTutorNameSelection(ActionEvent event) {
        String tutorName = tutorNameComboBox.getValue();
        if (tutorName != null && !tutorName.isEmpty()) {
            try {
                String tutorID = model.getTutorID(tutorName);
                tutorIDComboBox.setValue(tutorID);
                List<String> subjectNames = model.getAllSubjectNames(tutorID);
                view.updateSubjectName(subjectNames);
                List<String> subjectID = model.getAllSubjectIDs(tutorID);
                view.updateSubjectID(subjectID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSubjectIdSelection(ActionEvent event) {
        String subjectID = subjectIdComboBox.getValue();
        if (subjectID != null && !subjectID.isEmpty()) {
            try {
                String subjectName = model.getSubjectName(subjectID);
                subjectNameComboBox.setValue(subjectName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSubjectNameSelection(ActionEvent event) {
        String subjectName = subjectNameComboBox.getValue();
        if (subjectName != null && !subjectName.isEmpty()) {
            try {
                String subjectID = model.getSubjectID(subjectName);
                subjectIdComboBox.setValue(subjectID);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStartTimeSelection(ActionEvent event) {
        try {
            String time = view.getSelectedStartTime();
            String[] parts = time.split(":");
            LocalTime startTime = LocalTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            List<String> durations = model.getAvailableDurations(startTime);
            view.updateDurations(durations);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAddSession(ActionEvent event) {
        boolean isValid = true;
        view.clearAllErrorMessages();

        if (view.getSelectedTutorName() == null || view.getSelectedTutorID() == null ) {
            view.showErrorBelowComboBox(tutorIDComboBox, "Please select a tutor.");
            view.showErrorBelowComboBox(tutorNameComboBox, "Please select a tutor.");
            isValid = false;
        }

        if (view.getSelectedDate() == null) {
            System.out.println("DatePicker is empty, showing error.");
            view.showErrorBelowDatePicker(datePicker, "Please select a date.");
            datePicker.getParent().requestLayout();

            isValid = false;
        }

        if (view.getSelectedDate() == null) {
            view.showErrorBelowDatePicker(datePicker, "Please select a date.");
            isValid = false;
        }

        if (view.getSelectedStartTime() == null) {
            view.showErrorBelowComboBox(startTimeComboBox, "Please select a start time.");
            isValid = false;
        }

        if (view.getSelectedDuration() == null) {
            view.showErrorBelowComboBox(durationComboBox, "Please select a duration.");
            isValid = false;
        }

        if (view.getSelectedSubjectName() == null || view.getSelectedSubjectID() == null) {
            view.showErrorBelowComboBox(subjectNameComboBox, "Please select a subject.");
            view.showErrorBelowComboBox(subjectIdComboBox, "Please select a subject.");
            isValid = false;
        }

        if (view.getSelectedMode() == null) {
            view.showErrorBelowComboBox(sessionModeComboBox, "Please select a session mode.");
            isValid = false;
        }

        if (view.getSelectedType() == null) {
            view.showErrorBelowComboBox(sessionTypeComboBox, "Please select a session type.");
            isValid = false;
        }

        String maxStudentsInput = maxStudentsTextField.getText();
        if (maxStudentsInput.isEmpty()) {
            view.showErrorBelowTextField(maxStudentsTextField, "Please enter max number of students.");
            isValid = false;
        } else {
            try {
                int maxStudents = Integer.parseInt(maxStudentsInput);
                if (maxStudents <= 0) {
                    view.showErrorBelowTextField(maxStudentsTextField, "Value must be greater than 0.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                view.showErrorBelowTextField(maxStudentsTextField, "Only numeric values allowed.");
                isValid = false;
            }
        }

        String priceInput = sessionPriceTextField.getText();
        if (priceInput.isEmpty()) {
            view.showErrorBelowTextField(sessionPriceTextField, "Please enter a price.");
            isValid = false;
        } else {
            try {
                Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                view.showErrorBelowTextField(sessionPriceTextField, "Only numeric values allowed.");
                isValid = false;
            }
        }

        if (!isValid) return;

        try {
            model.addNewSession(
                    view.getSelectedTutorID(),
                    view.getSelectedDate(),
                    view.getSelectedStartTime(),
                    view.getSelectedDuration(),
                    view.getSelectedSubjectID(),
                    view.getSelectedMode(),
                    view.getSelectedType(),
                    view.getMaxStudents(),
                    view.getPrice()
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Session Added");
            alert.setHeaderText(null);
            alert.setContentText("The session has been added successfully.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Stage stage = (Stage) addSessionWindowButton.getScene().getWindow();
                    stage.close();
                }
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/add_new_session_window.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Session");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load Add Session window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void addSessionButtonHovered(MouseEvent event) {
        view.handleButtonHover(event);
    }

    @FXML
    private void addSessionButtonExited(MouseEvent event) {
        view.handleButtonExit(event);
    }
}