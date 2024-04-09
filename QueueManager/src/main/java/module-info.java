module org.example.queuemanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.queuemanager to javafx.fxml;
    exports org.example.queuemanager;
    exports org.example.queuemanager.backend.controllers;
    opens org.example.queuemanager.backend.controllers to javafx.fxml;
}