module Learnify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires java.naming;

    exports shared.interfaces;
    exports client;
    exports client.landingpage; // Export the landingpage package
    opens client.landingpage to javafx.fxml;
    opens client.landingpage.login to javafx.fxml;

}