package server.ippicker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.CountDownLatch;

public class IPPickerApp extends Application {
    public static String selectedIP;
    public static CountDownLatch latch;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/server/ip_picker.fxml"));
        Parent root = loader.load();

        IPPickerController controller = loader.getController();

        stage.setTitle("Select Server IP");
        stage.setScene(new Scene(root, 350, 250));
        stage.setResizable(false);

        stage.setOnHidden(e -> {
            selectedIP = controller.getSelectedIP();
            latch.countDown();
        });

        stage.show();
    }
}