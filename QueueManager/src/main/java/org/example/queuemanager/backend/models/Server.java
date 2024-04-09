package org.example.queuemanager.backend.models;

import org.example.queuemanager.backend.singletons.RepositorySingleton;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class  Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public int getWaitingPeriod() { return waitingPeriod.intValue(); }

    synchronized public void addTask(Task newTask) {
        if (newTask == null) {
            System.err.println("Can't add a new task.");
            return;
        }

        tasks.add(newTask);
        this.waitingPeriod.addAndGet(newTask.getTotalTime());
        System.out.println(newTask + " a fost trimis catre serverul " + this + ". Timpul nou de " +
                "asteptare pentru server este: " + getWaitingPeriod() + ", fiind rezultatul a " + getTasks().size() + " task-uri.");

        synchronized (StateManagerSingleton.class) {
            StateManagerSingleton.getProgressBars().get(this).setProgress(
                (tasks.size() * 1.0) / StateManagerSingleton.getMaxTaskPerServer()
            );

            StateManagerSingleton.getTextField().setText(
                RepositorySingleton.getSimulationRepository().displayServersData()
            );
        }
    }

    synchronized public BlockingQueue<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Task currentTask = tasks.peek();
                if(currentTask != null) {
                    Thread.sleep(currentTask.getTotalTime() * 1000L);

                    this.waitingPeriod.addAndGet(-currentTask.getTotalTime());
                    boolean ok = tasks.remove(currentTask);

                    synchronized (StateManagerSingleton.class) {
                        StateManagerSingleton.getProgressBars().get(this).setProgress(
                            (tasks.size() * 1.0) / StateManagerSingleton.getMaxTaskPerServer()
                        );

                        StateManagerSingleton.getTextField().setText(
                            RepositorySingleton.getSimulationRepository().displayServersData()
                        );
                    }

                    System.out.println(currentTask + " paraseste serverul. " + this + ". Timpul nou de " +
                            "asteptare pentru server este: " + getWaitingPeriod() + ", fiind rezultatul a " + getTasks().size() + " task-uri.");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

