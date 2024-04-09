package org.example.queuemanager.backend.policies;

import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.models.Task;

import java.util.List;

@FunctionalInterface
public interface StrategyPolicy {
     void addTask(List<Server> list, Task task) throws InterruptedException;

     default boolean sameStatuses(List<Server> list) {
          int waiting = list.getFirst().getWaitingPeriod();
          int size    = list.getFirst().getTasks().size();

          for(Server currentServer : list) {
               if(currentServer == list.getFirst()) continue;

               if(waiting != currentServer.getWaitingPeriod() || size != currentServer.getTasks().size()) {
                    return false;
               }
          }

          return true;
     }
}
