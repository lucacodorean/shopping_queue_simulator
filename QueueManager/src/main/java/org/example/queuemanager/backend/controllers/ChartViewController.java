package org.example.queuemanager.backend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.LineChart;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;

import java.net.URL;
import java.util.*;

public class ChartViewController implements Initializable {


    @FXML private LineChart<Number, Number> tbServiceTime;
    @FXML private LineChart<Number, Number> tbTotalTime;
    @FXML private LineChart<Number, Number> tbArrivalTime;
    @Override
    public void initialize(URL url, ResourceBundle rs) {

        populateChart(tbServiceTime, getServiceTimeMap(),
            "Task distribution over service time spent in server",
            "Service time",
            "Tasks count",
            "Tasks over time"
        );

        populateChart(tbTotalTime, getTotalTimeMap(),
            "Task distribution over total time spent in server",
            "Waiting time",
            "Tasks count",
            "Tasks over time"
        );

        populateChart(tbArrivalTime, getArrivalTimeMap(),
                "Task distribution by arrival time",
                "Arrival time",
                "Tasks count",
                "Tasks over time"
        );
    }

    private void populateChart(XYChart<Number, Number> chart, Map<Integer, Integer> dataBuffer,
                               String title, String xAxisText, String yAxisText,
                               String seriesDescription) {

        chart.setTitle(title);
        chart.getXAxis().setLabel(xAxisText);
        chart.getYAxis().setLabel(yAxisText);

        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        dataBuffer.forEach((key, value) -> {
            series.getData().add(new XYChart.Data<>(key, value));
        });

        series.setName(seriesDescription);
        chart.getData().add(series);
    }

    private Map<Integer,Integer> getServiceTimeMap() {
        Map<Integer,Integer> data = new HashMap<>(StateManagerSingleton.getMaxProcessingTime());
        for(int i = 0; i<StateManagerSingleton.getMaxProcessingTime(); i++) data.put(i, 0);

        StateManagerSingleton.getTasks().forEach((curentTask) -> {
            data.replace(curentTask.getServiceTime(), data.get(curentTask.getServiceTime()) + 1);
        });

        return data;
    }

    private Map<Integer,Integer> getTotalTimeMap() {
        int size = StateManagerSingleton.getMaxProcessingTime() + StateManagerSingleton.getMaxArrivalTime();
        Map<Integer,Integer> data = new HashMap<>(size);
        for(int i = 0; i<size; i++) data.put(i, 0);

        StateManagerSingleton.getTasks().forEach((curentTask) -> {
            data.replace(curentTask.getTotalTime(), data.get(curentTask.getTotalTime()) + 1);
        });

        return data;
    }

    private Map<Integer, Integer> getArrivalTimeMap() {
        Map<Integer,Integer> data = new HashMap<>(StateManagerSingleton.getMaxArrivalTime());
        for(int i = 0; i<StateManagerSingleton.getMaxArrivalTime(); i++) data.put(i, 0);

        StateManagerSingleton.getTasks().forEach((curentTask) -> {
            data.replace(curentTask.getArrivalTime(), data.get(curentTask.getArrivalTime()) + 1);
        });

        return data;
    }
}
