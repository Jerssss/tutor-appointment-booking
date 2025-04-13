module Learnify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;

    exports shared.interfaces;
    exports client;
    exports client.landingpage; // Export the landingpage package
    opens client.landingpage to javafx.fxml;
}