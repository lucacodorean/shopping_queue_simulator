package org.example.queuemanager.backend.policies.Implementation;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.policies.PoliciesEnum;
import org.example.queuemanager.backend.policies.TimeStrategyPolicy;
import org.example.queuemanager.backend.singletons.PolicySingleton;
import org.example.queuemanager.backend.singletons.ServiceSingleton;
import org.example.queuemanager.backend.singletons.StateManagerSingleton;

import java.util.ArrayList;
import java.util.List;

public class TimeStrategyPolicyImpl implements TimeStrategyPolicy {

    @Override
    synchronized public void addTask(List<Server> list, Task task) throws InterruptedException {

        if(list == null || task == null) {
            System.err.println("List or task is null.");
            return;
        }

        Server selectedServer = null;
        List<Server> candidateServers =  new ArrayList<>();
        int shortestTime = Integer.MAX_VALUE;
        boolean empty = false;

        for(Server currentServer : list) {
            if(currentServer.getTasks().isEmpty()) {
                selectedServer = currentServer;
                empty = true;
                break;
            }

            int currentTime = currentServer.getWaitingPeriod();

            if(currentTime == shortestTime) {
                candidateServers.add(currentServer);
                continue;
            }

            if(currentTime < shortestTime) {
                shortestTime = currentTime;
                candidateServers.clear();
                candidateServers.add(currentServer);
            }
        }

        if(empty) selectedServer.addTask(task);

        else if(!candidateServers.isEmpty()) {
            if(sameStatuses(list)) {
                list.getFirst().addTask(task);
                return;
            }

            System.out.println("Strategia de distribuire a fost modificata: " + PoliciesEnum.SHORTEST_QUEUE.name() + ".");
            ServiceSingleton.getSchedulerService().changeStrategy(PoliciesEnum.SHORTEST_QUEUE);
            PolicySingleton.getShortestQueuePolicy().addTask(candidateServers, task);
        }

        else System.err.println("There is no slot for the task in any of the servers.");
    }
}
