package org.example.queuemanager.backend.services.implementation;

import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.repositories.SimulationManagerRepository;
import org.example.queuemanager.backend.services.SchedulerService;
import org.example.queuemanager.backend.services.SimulationManagerService;
import org.example.queuemanager.backend.singletons.RepositorySingleton;

import java.util.List;

public class SimulationManagerServiceImpl implements SimulationManagerService {

    @Override
    public void run() {
        Thread currentThread = new Thread(RepositorySingleton.getSimulationRepository());
        currentThread.start();
    }

    @Override public List<Server> getServers() {
        return RepositorySingleton.getSchedulerRepository().getServers();
    }
}
