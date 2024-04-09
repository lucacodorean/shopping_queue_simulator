package org.example.queuemanager.backend.singletons;

import org.example.queuemanager.backend.services.SchedulerService;
import org.example.queuemanager.backend.services.SimulationManagerService;
import org.example.queuemanager.backend.services.implementation.SchedulerServiceImpl;
import org.example.queuemanager.backend.services.implementation.SimulationManagerServiceImpl;

public class ServiceSingleton {
    private static final SchedulerService schedulerService = new SchedulerServiceImpl();
    private static final SimulationManagerService simulationManager =
            new SimulationManagerServiceImpl();
    public static SchedulerService getSchedulerService() { return schedulerService; }
    public static SimulationManagerService getSimulationManager() { return simulationManager; }
}
