package org.example.queuemanager.backend.singletons;

import org.example.queuemanager.backend.policies.Implementation.ShortestQueueStrategyPolicyImpl;
import org.example.queuemanager.backend.policies.Implementation.TimeStrategyPolicyImpl;
import org.example.queuemanager.backend.policies.ShortestQueueStrategyPolicy;
import org.example.queuemanager.backend.policies.TimeStrategyPolicy;

public class PolicySingleton {
    private static final TimeStrategyPolicy timeStrategyPolicy = new TimeStrategyPolicyImpl();
    private static final ShortestQueueStrategyPolicy shortestQueuePolicy =
            new ShortestQueueStrategyPolicyImpl();

    public static TimeStrategyPolicy getTimeStrategyPolicy() { return timeStrategyPolicy; }

    public static ShortestQueueStrategyPolicy getShortestQueuePolicy() { return shortestQueuePolicy; }
}
