package org.example.queuemanager.backend.services.implementation;

import org.example.queuemanager.backend.models.Task;
import org.example.queuemanager.backend.policies.PoliciesEnum;
import org.example.queuemanager.backend.services.SchedulerService;
import org.example.queuemanager.backend.singletons.RepositorySingleton;

public class SchedulerServiceImpl implements SchedulerService {

    @Override public void dispatchTask(Task task) {
        if(task == null) {
            System.err.println("Task can't be dispatched due to its null state.");
            return;
        }
        RepositorySingleton.getSchedulerRepository().dispatchTask(task);
    }

    @Override public void changeStrategy(PoliciesEnum strategyPolicy) {
        RepositorySingleton.getSchedulerRepository().changeStrategy(strategyPolicy);
    }
}
