package org.example.queuemanager.backend.policies.Implementation;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.policies.PoliciesEnum;
import org.example.queuemanager.backend.policies.ShortestQueueStrategyPolicy;
import org.example.queuemanager.backend.singletons.PolicySingleton;
import org.example.queuemanager.backend.singletons.ServiceSingleton;

import java.util.ArrayList;
import java.util.List;

public class ShortestQueueStrategyPolicyImpl implements ShortestQueueStrategyPolicy {
    @Override
    synchronized public void addTask(List<Server> list, Task task) throws InterruptedException {
        if(list == null || task == null) {
            System.err.println("List or task is null.");
            return;
        }

        Server  selectedServer = null;
        boolean selected = false;
        int shortestLength = Integer.MAX_VALUE;
        List<Server> candidateServers =  new ArrayList<>();

        for(Server currentServer : list) {
            if(currentServer.getTasks().isEmpty()) {
                selectedServer = currentServer;
                selected = true;
                break;
            }

            if(currentServer.getTasks().size() == shortestLength) {
                candidateServers.add(currentServer);
                continue;
            }

            if(currentServer.getTasks().size() < shortestLength) {
                shortestLength = currentServer.getTasks().size();
                candidateServers.clear();
                candidateServers.add(currentServer);
            }
        }

        if(selected) selectedServer.addTask(task);

        else if(!candidateServers.isEmpty()){
            if(sameStatuses(list)) {
                list.getFirst().addTask(task);
                return;
            }

            System.out.println("Strategia de distribuire a fost modificata: " + PoliciesEnum.SHORTEST_TIME.name() + ".");
            ServiceSingleton.getSchedulerService().changeStrategy(PoliciesEnum.SHORTEST_TIME);
            PolicySingleton.getTimeStrategyPolicy().addTask(candidateServers, task);
        }

        else System.err.println("There is no slot for the task in any of the servers.");
    }
}
