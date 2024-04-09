package org.example.queuemanager.backend.singletons;

import org.example.queuemanager.backend.repositories.SchedulerRepository;
import org.example.queuemanager.backend.repositories.SimulationManagerRepository;
import org.example.queuemanager.backend.repositories.implementation.SchedulerRepositoryImpl;
import org.example.queuemanager.backend.repositories.implementation.SimulationManagerRepositoryImpl;

public class RepositorySingleton {
    private static final SchedulerRepository schedulerRepository
            = new SchedulerRepositoryImpl(
                StateManagerSingleton.getNumberOfServers(),
                StateManagerSingleton.getMaxTaskPerServer());
    private static final SimulationManagerRepository simulationRepository = new SimulationManagerRepositoryImpl();
    public static SchedulerRepository getSchedulerRepository() { return schedulerRepository; }
    public static SimulationManagerRepository getSimulationRepository() { return simulationRepository; }
}
