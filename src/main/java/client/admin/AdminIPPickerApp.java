package client.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class AdminIPPickerApp extends Application {
    public static String selectedIP;
    public static CountDownLatch latch;

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/fxml/ippicker/admin_ip_picker.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML file not found at /fxml/client/admin_ip_picker.fxml");
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        AdminIPPickerController controller = loader.getController();

        stage.setTitle("Select Admin Client IP");
        stage.setScene(new Scene(root, 350, 250));
        stage.setResizable(false);

        stage.setOnHidden(e -> {
            selectedIP = controller.getSelectedIP();
            latch.countDown();
        });

        stage.show();
    }
}