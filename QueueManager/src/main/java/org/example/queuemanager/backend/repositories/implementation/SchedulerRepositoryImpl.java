package org.example.queuemanager.backend.repositories.implementation;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.policies.PoliciesEnum;
import org.example.queuemanager.backend.policies.StrategyPolicy;
import org.example.queuemanager.backend.singletons.PolicySingleton;
import org.example.queuemanager.backend.repositories.SchedulerRepository;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;

import java.util.ArrayList;
import java.util.List;

public class SchedulerRepositoryImpl implements SchedulerRepository {

    private List<Server> servers;
    private Integer maxNoServers;
    private Integer maxTasksPerServer;
    private StrategyPolicy strategy;

    public SchedulerRepositoryImpl(Integer maxNoServers, Integer maxTasksPerServer) {
        if(maxNoServers < 1 || maxTasksPerServer < 1) {
            System.err.println("The scheduler can't have a negative number of servers, nor tasks.");
            return;
        }

        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        for(int currentServerIndex = 0; currentServerIndex<this.maxNoServers; currentServerIndex++) {
            Server currentServer = new Server();
            addServer(currentServer);
            new Thread(currentServer);
        }
    }

    private void addServer(Server parameter) {
        if(parameter == null) {
            System.err.println("Can't add this server because is null.");
            return;
        }

        if(this.maxNoServers < servers.size()) {
            System.err.println("A new server can't be added because the limit has been reached.");
            return;
        }

        for(Server currentServer : servers) {
            if(currentServer == parameter) {
                System.err.println(
                    "Can't add this server because it is already in this scheduler's server list."
                );
                return;
            }
        }

        servers.add(parameter);
    }

    public void changeStrategy(PoliciesEnum strategyPolicy) {

        if(strategyPolicy == null) {
            System.err.println("The strategy is null");
            return;
        }

        if(strategyPolicy == PoliciesEnum.SHORTEST_QUEUE) {
           strategy = PolicySingleton.getShortestQueuePolicy();
           StateManagerSingleton.setStrategy(PoliciesEnum.SHORTEST_QUEUE);
           return;
        }

        if(strategyPolicy == PoliciesEnum.SHORTEST_TIME) {
            strategy = PolicySingleton.getTimeStrategyPolicy();
            StateManagerSingleton.setStrategy(PoliciesEnum.SHORTEST_TIME);
        }
    }

    synchronized public void dispatchTask(Task task) {
        if(task == null) {
            System.err.println("Task is null. It can't be dispatched");
            return;
        }

        List<Server> serversNotFull = new ArrayList<>();

        try {
            servers.forEach((currentServer) -> {
                if(currentServer.getTasks().size() < this.maxTasksPerServer) {
                    serversNotFull.add(currentServer);
                }
            });

            strategy.addTask(serversNotFull, task);
        } catch (ClassCastException | InterruptedException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void setStrategy(PoliciesEnum strategy) {
        if(strategy == PoliciesEnum.SHORTEST_QUEUE) {
            this.strategy = PolicySingleton.getShortestQueuePolicy();
        }
        else if(strategy == PoliciesEnum.SHORTEST_TIME) {
            this.strategy = PolicySingleton.getTimeStrategyPolicy();
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
