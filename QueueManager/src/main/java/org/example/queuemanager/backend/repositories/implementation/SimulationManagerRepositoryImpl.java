package org.example.queuemanager.backend.repositories.implementation;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.singletons.RepositorySingleton;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;
import org.example.queuemanager.backend.repositories.SchedulerRepository;
import org.example.queuemanager.backend.repositories.SimulationManagerRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.time.Instant;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationManagerRepositoryImpl implements SimulationManagerRepository {

    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int maxArrivalTime;
    public int minArrivalTime;
    private final SchedulerRepository scheduler;
    private final List<Task> generatedTasks;

    public SimulationManagerRepositoryImpl() {

        timeLimit           = StateManagerSingleton.getTimeLimit();
        numberOfClients     = StateManagerSingleton.getNumberOfClients();
        numberOfServers     = StateManagerSingleton.getNumberOfServers();
        maxProcessingTime   = StateManagerSingleton.getMaxProcessingTime();
        minProcessingTime   = StateManagerSingleton.getMinProcessingTime();
        minArrivalTime      = StateManagerSingleton.getMinArrivalTime();
        maxArrivalTime      = StateManagerSingleton.getMaxArrivalTime();
        generatedTasks      = new CopyOnWriteArrayList<>();

        scheduler           = RepositorySingleton.getSchedulerRepository();
        scheduler.setStrategy(StateManagerSingleton.getStrategy());
        generatedTasks.addAll(generateNRandomTasks());

        StateManagerSingleton.setTasks(List.copyOf(generatedTasks));

        for(int i = 0; i<numberOfServers; i++) {
            Thread currentThread = new Thread(scheduler.getServers().get(i));
            currentThread.start();
        }
    }
    private List<Task> generateNRandomTasks() {
        List<Task> generatedTasks = new CopyOnWriteArrayList<>();
        int N = numberOfClients;
        Random rand = new Random(Instant.now().getEpochSecond());
        while(N > 0) {
            int processingTime = rand.nextInt(minProcessingTime, maxProcessingTime);
            int arrivalTime    = rand.nextInt(minArrivalTime,    maxArrivalTime);

            if(processingTime + arrivalTime > timeLimit) continue;

            Task task = new Task(numberOfClients - N, arrivalTime, processingTime);

            generatedTasks.add(task);
            N--;
            if(generatedTasks.size() > 1) {
                generatedTasks.sort((a1, a2) -> {
                    if(a1 == null || a2 == null) return 0;
                    return a1.getArrivalTime() - a2.getArrivalTime();
                });
            }
        }

        return generatedTasks;
    }

    public String displayServersData() {
        StringBuilder result;

        result = new StringBuilder();

        for(Server currentServer: scheduler.getServers()) {
            result.append("\nServer:").append(currentServer).append("\n");
            for(Task currentTask : currentServer.getTasks()) {
                result.append("\n\t").append(currentTask.toString()).append(", ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    private String displayEvaluation() {
        StringBuilder result;

        result = new StringBuilder();

        double waitingTimeAverage = 0.0, serviceTimeAverage = 0.0;

        for(Task currentTask : generatedTasks) {
            waitingTimeAverage += currentTask.getTotalTime();
            serviceTimeAverage += currentTask.getServiceTime();
        }
        result.append("\n");

        result
            .append(LocalDateTime.now()).append(" SIMULATION RESULT: ")
            .append("\n\tAverage waiting time: " + waitingTimeAverage / StateManagerSingleton.getNumberOfClients())
            .append("\n\tAverage service time: " + serviceTimeAverage / StateManagerSingleton.getNumberOfClients());

        return result.toString();
    }

    private void writeAnalysis() {
        StateManagerSingleton.setReportFile(
                new File("src/main/resources/org/example/queuemanager/simulations/",
                        "simulation_report_"+ generateKey(25) + ".txt")
        );

        try {
            if(StateManagerSingleton.getFile().createNewFile()) {
                StateManagerSingleton.setFileWriter();
                StateManagerSingleton.getCurrentFileWriter().write(displayEvaluation());
                StateManagerSingleton.getCurrentFileWriter().close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void run() {
        writeAnalysis();

        int currentTime = 1;
        try {
            System.out.println(currentTime);
            while (currentTime <= timeLimit) {

                if (!generatedTasks.isEmpty()) {
                    for (Task currentTask : generatedTasks) {
                        if (currentTask == null) continue;

                        if (currentTask.getArrivalTime() == currentTime) {
                            Thread.sleep(500L);
                            scheduler.dispatchTask(currentTask);
                            generatedTasks.remove(currentTask);
                        }
                    }
                }
                Thread.sleep(1000L);
                currentTime++;
            }
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
