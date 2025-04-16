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
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class AdminAddSessionPopUpController implements Initializable {
    private final AdminAddSessionPopUpModel model;
    private AdminAddSessionPopUpView view;
    private final AdminService service = new AdminServiceImpl();

    @FXML private ComboBox<String> tutorComboBox;
    @FXML private ComboBox<String> subjectComboBox;
    @FXML private ComboBox<String> sessionModeComboBox;
    @FXML private ComboBox<String> sessionTypeComboBox;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button addSessionWindowButton;
    @FXML private TextField sessionPriceTextField;
    @FXML private TextField maxStudentsTextField;

    public AdminAddSessionPopUpController() {
        this.model = new AdminAddSessionPopUpModel(service);
//        showWindow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.view = new AdminAddSessionPopUpView(
                tutorComboBox,
                subjectComboBox,
                sessionModeComboBox,
                sessionTypeComboBox,
                startTimeComboBox,
                durationComboBox,
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
    }

    private void initializeData() throws RemoteException {
        List<String> tutors = model.getTutorNames();
        List<String> subjects = model.getSubjectNames();
        view.initializeComboBoxes(tutors, subjects);
    }

    private void setupEventHandlers() {
        view.setTutorSelectionHandler(this::handleTutorSelection);
        view.setAddSessionButtonAction(this::handleAddSession);
        view.setDateSelectionHandler(this::handleDateSelection);
        view.setStartTimeSelectionHandler(this::handleStartTimeSelection);
    }

    private void handleTutorSelection(ActionEvent event) {
//        try {
            String tutor = view.getSelectedTutor();
//            List<String> times = model.getAvailableTimeTutor(tutor);
//            view.updateStartTimes(times);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
    }

    private void handleDateSelection(ActionEvent event) {
        try {
            String tutor = view.getSelectedTutor();
            String date = view.getSelectedDate().toString();
            List<String> times = model.getAvailableTimeTutor(tutor, date);
            view.updateStartTimes(times);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
        try {
            model.addNewSession(
                    view.getSelectedTutor(),
                    view.getSelectedDate(),
                    view.getSelectedStartTime(),
                    view.getSelectedDuration(),
                    view.getSelectedSubject(),
                    view.getSelectedMode(),
                    view.getSelectedType(),
                    view.getMaxStudents(),
                    view.getPrice()
            );
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