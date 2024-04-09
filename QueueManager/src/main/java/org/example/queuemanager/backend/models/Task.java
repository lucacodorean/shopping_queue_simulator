package org.example.queuemanager.backend.models;

public class Task {
    private final int id;
    private final int arrivalTime;
    private final int serviceTime;

    public Task(Integer id, Integer arrivalTime, Integer serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getArrivalTime() { return arrivalTime; }
    public int getServiceTime() { return serviceTime; }
    public int getTotalTime() { return this.arrivalTime + this.serviceTime; }

    @Override
    public String toString() {
        return "Task ID: " + this.id + " | arrival_time: " + this.arrivalTime + " & " +
                "service_time: " + this.serviceTime;
    }
}
