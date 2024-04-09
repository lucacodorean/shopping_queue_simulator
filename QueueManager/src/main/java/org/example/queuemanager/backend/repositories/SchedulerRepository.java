package org.example.queuemanager.backend.repositories;

import org.example.queuemanager.backend.models.Server;
import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.policies.PoliciesEnum;

import java.util.List;

public interface SchedulerRepository {
    void changeStrategy(PoliciesEnum strategyPolicy);
    void dispatchTask(Task task);
    List<Server> getServers();
    void setStrategy(PoliciesEnum strategyPolicy);
}
