package org.example.queuemanager.backend.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.queuemanager.HelloApplication;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.policies.PoliciesEnum;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;
import org.example.queuemanager.backend.singletons.ServiceSingleton;
import org.example.queuemanager.backend.services.SimulationManagerService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class HelloController implements Initializable {
    @FXML private TextField minArrivalTime;
    @FXML private TextField maxArrivalTime;
    @FXML private TextField txtTimeLimit;
    @FXML private TextField maxProcessingTime;
    @FXML private TextField minProcessingTime;
    @FXML private TextField numberOfServers;
    @FXML private TextField numberOfClients;
    @FXML private TextField maxTasksPerServer;
    @FXML private VBox vBoxBars;
    @FXML private ScrollPane scrollBarBox;
    @FXML private TextArea txtLogs;
    @FXML private Button btnAnalysis;
    private Map<Server, ProgressBar> serversProgress;

    @Override
    public void initialize(URL url, ResourceBundle rs) {
        scrollBarBox.setVisible(false);
        txtLogs.setVisible(false);
        vBoxBars.setSpacing(2.5);
        btnAnalysis.setDisable(true);
    }

    private Boolean evaluateFields() {
        return  maxProcessingTime.getText().isBlank() || minProcessingTime.getText().isBlank() ||
                maxArrivalTime.getText().isBlank()    || minArrivalTime.getText().isBlank() ||
                numberOfServers.getText().isBlank()   || numberOfClients.getText().isBlank() ||
                txtTimeLimit.getText().isBlank()      || maxTasksPerServer.getText().isBlank();
    }

    public void simulate(ActionEvent e) {
        if(evaluateFields()) {
            System.err.println("Error");
            return;
        }

        StateManagerSingleton.setMaxArrivalTime(getMaxArrivalTime());
        StateManagerSingleton.setMinArrivalTime(getMinArrivalTime());
        StateManagerSingleton.setMaxProcessingTime(getMaxProcessingTime());
        StateManagerSingleton.setMinProcessingTime(getMinProcessingTime());
        StateManagerSingleton.setStrategy(PoliciesEnum.SHORTEST_TIME);
        StateManagerSingleton.setTimeLimit(getTimeLimit());
        StateManagerSingleton.setNumberOfServers(getNumberOfServers());
        StateManagerSingleton.setNumberOfClients(getNumberOfClients());
        StateManagerSingleton.setMaxTasksPerServer(getMaxTasksPerServer());

        SimulationManagerService simulationManager = ServiceSingleton.getSimulationManager();

        serversProgress = new HashMap<>();
        simulationManager.getServers().forEach(
            (server) -> {
                ProgressBar newProgressBar = new ProgressBar(0.01);
                newProgressBar.setPrefSize(vBoxBars.getPrefWidth(), 50);
                newProgressBar.setVisible(true);
                vBoxBars.getChildren().add(newProgressBar);
                newProgressBar.setStyle("-fx-accent: #886797");
                serversProgress.put(server, newProgressBar);
            }
        );

        txtLogs.setVisible(true);
        scrollBarBox.setVisible(true);
        StateManagerSingleton.setText(txtLogs);
        vBoxBars.setPrefHeight(50 * serversProgress.size());
        StateManagerSingleton.setProgressBars(serversProgress);
        btnAnalysis.setDisable(false);
        new Thread(simulationManager).start();
    }

    public void openAnalysis(ActionEvent e) {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("chart-view.fxml"));
                stage.setScene(new Scene(loader.load()));
                stage.show();
                btnAnalysis.setDisable(true);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private int getMinProcessingTime() { return Integer.parseInt(minProcessingTime.getText()); }
    private int getTimeLimit() { return Integer.parseInt(txtTimeLimit.getText()); }
    private int getMaxProcessingTime() {return Integer.parseInt(maxProcessingTime.getText()); }
    private int getNumberOfServers() { return Integer.parseInt(numberOfServers.getText()); }
    private int getNumberOfClients() { return Integer.parseInt(numberOfClients.getText()); }
    private int getMinArrivalTime() { return Integer.parseInt(minArrivalTime.getText()); }
    private int getMaxArrivalTime() { return Integer.parseInt(maxArrivalTime.getText()); }
    private int getMaxTasksPerServer() { return Integer.parseInt(maxTasksPerServer.getText()); }
}
