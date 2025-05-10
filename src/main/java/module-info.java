module Learnify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires java.compiler;
    requires mysql.connector.j;

    exports shared.interfaces;
    exports client;
    exports client.admin to javafx.graphics;
    exports client.landingpage to javafx.graphics;
    exports server;
    exports server.ippicker;
    exports client.student.controller to javafx.fxml;
    exports client.tutor.controller to javafx.fxml;

    opens server.ippicker to javafx.fxml;
    opens client.landingpage to javafx.fxml;
    opens client.landingpage.login to javafx.fxml;
    opens client.admin to javafx.fxml;
    opens client.admin.view to javafx.fxml;
    opens client.admin.controller to javafx.fxml;
    opens client.student.view to javafx.fxml;
    opens client.tutor.view to javafx.fxml;
    opens shared.classes to javafx.base;
}