package org.example.queuemanager.backend.singletons;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.policies.PoliciesEnum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StateManagerSingleton {
    private static int timeLimit;
    private static int maxProcessingTime;
    private static int minProcessingTime;
    private static int numberOfServers;
    private static int numberOfClients;
    private static int maxArrivalTime;
    private static int minArrivalTime;
    private static int maxTaskPerServer;
    private static PoliciesEnum strategy;

    private static Map<Server, ProgressBar> progressBars;

    private static TextArea textField;

    private static List<Task> tasks;

    private static File currentReportFile;

    private static FileWriter currentFileWriter;

    public static void setTasks(List<Task> tasks) {
        StateManagerSingleton.tasks = tasks;
    }

    public static void setTimeLimit(int timeLimit) {
        StateManagerSingleton.timeLimit = timeLimit;
    }

    public static void setMaxProcessingTime(int maxProcessingTime) {
        StateManagerSingleton.maxProcessingTime = maxProcessingTime;
    }

    public static void setMinProcessingTime(int minProcessingTime) {
        StateManagerSingleton.minProcessingTime = minProcessingTime;
    }

    public static void setNumberOfServers(int numberOfServers) {
        StateManagerSingleton.numberOfServers = numberOfServers;
    }

    public static void setNumberOfClients(int numberOfClients) {
        StateManagerSingleton.numberOfClients = numberOfClients;
    }

    public static void setMaxArrivalTime(int maxArrivalTime) {
        StateManagerSingleton.maxArrivalTime = maxArrivalTime;
    }

    public static void setMinArrivalTime(int minArrivalTime) {
        StateManagerSingleton.minArrivalTime = minArrivalTime;
    }

    public static void setStrategy(PoliciesEnum strategy) {
        StateManagerSingleton.strategy = strategy;
    }

    public static void setProgressBars(Map<Server, ProgressBar> list) {
        StateManagerSingleton.progressBars = list;
    }

    public static void setMaxTasksPerServer(Integer maxTasksPerServer) {
        StateManagerSingleton.maxTaskPerServer = maxTasksPerServer;
    }

    public static void setReportFile(File file) {
        StateManagerSingleton.currentReportFile = file;
    }

    public static void setFileWriter() throws IOException {
        StateManagerSingleton.currentFileWriter = new FileWriter(currentReportFile);
    }

    public static void setText(TextArea textField) {
        StateManagerSingleton.textField = textField;
    }

    public static int getTimeLimit() { return timeLimit; }
    public static PoliciesEnum getStrategy() { return strategy; }
    public static int getNumberOfClients() { return numberOfClients; }
    public static int getNumberOfServers() { return numberOfServers; }
    public static int getMaxProcessingTime() { return maxProcessingTime; }
    public static int getMinProcessingTime() { return minProcessingTime; }
    public static int getMaxArrivalTime() { return maxArrivalTime; }
    public static int getMinArrivalTime() { return minArrivalTime;}
    public static Map<Server, ProgressBar> getProgressBars() { return progressBars; }
    public static List<Task> getTasks() { return tasks; }
    public static int getMaxTaskPerServer() { return maxTaskPerServer; }
    public static TextArea getTextField() { return textField; }
    public static File getFile() { return currentReportFile; }
    public static FileWriter getCurrentFileWriter() { return currentFileWriter; }
}
