package client.student.view;

import client.AdminClient;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class StudentMainMenuView {

    @FXML
    private Button studentButton;
    @FXML
    private Button tutorButton;
    @FXML
    private Button sessionsButton;
    @FXML
    private Button subjectButton;
    @FXML
    private Button lessonPlanButton;
    @FXML
    private Button paymentButton;

    @FXML
    private Label headerDateLabel;

    @FXML
    private Label headerNameLabel;

    @FXML
    private Label headerTimeLabel;

    @FXML
    private Button logOutButton;


    @FXML
    private Label offlineLabel;

    @FXML
    private Label onlineLabel;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button currentlyHighlightedButton;

    // Timer to periodically check server status
    private Timer serverStatusTimer;

    /**
     * Initializes the view controller.
     * Sets up date/time display and starts server status monitoring.
     */
    public void initialize() {
        initializeDateTime();
        startServerStatusChecker();
    }

    /**
     * Starts a timer to periodically check server connection status.
     * Updates UI labels based on server availability.
     */
    private void startServerStatusChecker() {
        serverStatusTimer = new Timer(true);
        serverStatusTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean isServerOnline = checkServerStatus();
                updateServerStatusLabels(isServerOnline);
            }
        }, 0, 5000); // Check every 5 seconds
    }

    /**
     * Checks if the server is reachable via RMI.
     * @return true if server is online, false otherwise
     */
    private boolean checkServerStatus() {
        try {
            Registry registry = LocateRegistry.getRegistry(AdminClient.getServerIP(), 1099);
            registry.lookup("authentication"); // Try to look-up a service
            return true; // Server is online
        } catch (RemoteException | NotBoundException e) {
            return false; // Server is offline
        }
    }

    /**
     * Updates the server status indicators in the UI.
     * @param isServerOnline Current server connection status
     */
    private void updateServerStatusLabels(boolean isServerOnline) {
        javafx.application.Platform.runLater(() -> {
            if (isServerOnline) {
                onlineLabel.setVisible(true);
                onlineLabel.setManaged(true); // Include in layout
                offlineLabel.setVisible(false);
                offlineLabel.setManaged(false); // Exclude from layout
            } else {
                onlineLabel.setVisible(false);
                onlineLabel.setManaged(false); // Exclude from layout
                offlineLabel.setVisible(true);
                offlineLabel.setManaged(true); // Include in layout
            }
        });
    }

    /**
     * Loads a new view into the main content area.
     * @param fxmlFile Path to the FXML file to load
     */
    private void loadView(String fxmlFile) {
        try {
            System.out.println("[SERVER] Loading FXML: " + fxmlFile);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            if (fxmlLoader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found: " + fxmlFile);
            }
            VBox view = fxmlLoader.load();
            rootPane.setCenter(view);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            showError("Failed to load view: " + fxmlFile);
        }
    }


    /**
     * Sets the name of the logged-in admin in the header.
     * @param name The admin's name to display
     */
    public void setLoggedInUserName(String name) {
        headerNameLabel.setText(name);
    }

    /**
     * Initializes and starts updating the date/time display.
     */
    public void initializeDateTime() {
        updateDateTime();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateDateTime();
            }
        }, 0, 1000);
    }

    /**
     * Updates the date and time display labels.
     */
    private void updateDateTime() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        javafx.application.Platform.runLater(() -> {
            headerDateLabel.setText(currentDate.format(dateFormatter));
            headerTimeLabel.setText(currentTime.format(timeFormatter));
        });
    }

    /**
     * Sets action handler for Add Terminal button.
     * @param event The event handler to set
     */


    public void setActionStudentButton(EventHandler<ActionEvent> event) {
        studentButton.setOnAction(event1 -> {
            highlightButton(studentButton);
            loadView("/fxml/admin/student_list_pane.fxml");
        });
    }
    public void setActionTutorButton(EventHandler<ActionEvent> event) {
        tutorButton.setOnAction(event1 -> {
            highlightButton(tutorButton);
            loadView("/fxml/admin/tutor_list_pane.fxml");
        });
    }
    public void setActionSessionsButton(EventHandler<ActionEvent> event) {
        sessionsButton.setOnAction(event1 -> {
            highlightButton(sessionsButton);
            loadView("/fxml/admin/sessions_pane.fxml");
        });
    }
    public void setActionSubjectButton(EventHandler<ActionEvent> event) {
        subjectButton.setOnAction(event1 -> {
            highlightButton(subjectButton);
            loadView("/fxml/admin/subject_pane.fxml");
        });
    }
    public void setActionLessonPlanButton(EventHandler<ActionEvent> event) {
        lessonPlanButton.setOnAction(event1 -> {
            highlightButton(lessonPlanButton);
            loadView("/fxml/admin/lesson_plan_pane.fxml");
        });
    }
    public void setActionPaymentButton(EventHandler<ActionEvent> event) {
        paymentButton.setOnAction(event1 -> {
            highlightButton(paymentButton);
            loadView("/fxml/admin/payment_history_pane.fxml");
        });
    }

    /**
     * Sets action handler for Logout button.
     * @param event The event handler to set
     */
    public void setActionLogoutButton(EventHandler<ActionEvent> event) {
        logOutButton.setOnAction(event);
    }


    /**
     * Displays an error alert dialog.
     * @param message The error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Highlights the currently selected navigation button.
     * @param button The button to highlight
     */
    private void highlightButton(Button button) {
        //remove the highlight from the previously highlighted button

        if (currentlyHighlightedButton != null) {
            currentlyHighlightedButton.getStyleClass().remove("highlighted-button");
        }

        //highlight the new button
        button.getStyleClass().add("highlighted-button");

        //updates the currently highlighted button
        currentlyHighlightedButton = button;
    }
    public void logOutButtonExited() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), logOutButton);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
    public void logOutButtonHovered() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), logOutButton);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setCycleCount(1);
        st.setAutoReverse(false);
        st.play();
    }
}
