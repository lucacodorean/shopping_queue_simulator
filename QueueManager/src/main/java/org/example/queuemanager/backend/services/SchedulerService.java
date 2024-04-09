package org.example.queuemanager.backend.services;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.policies.PoliciesEnum;

public interface SchedulerService {
    void dispatchTask(Task task);
    void changeStrategy(PoliciesEnum strategyPolicy);
}
