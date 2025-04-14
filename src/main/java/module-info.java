module Learnify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires java.compiler;

    exports shared.interfaces;
    exports client;
    exports client.landingpage;

    opens client.landingpage to javafx.fxml;
    opens client.landingpage.login to javafx.fxml;
    opens client.admin.view to javafx.fxml;
}
