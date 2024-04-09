package org.example.queuemanager.backend.services;

import org.example.queuemanager.backend.models.Server;

import java.util.List;

public interface SimulationManagerService extends Runnable {
    @Override void run();
    List<Server> getServers();
}
