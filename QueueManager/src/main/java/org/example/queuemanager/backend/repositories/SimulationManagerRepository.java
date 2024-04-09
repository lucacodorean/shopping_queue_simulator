package org.example.queuemanager.backend.repositories;

import java.util.UUID;

public interface SimulationManagerRepository extends Runnable {
    String displayServersData();

    default String generateKey(Integer limit) {
        return UUID.randomUUID().toString().replaceAll("_", "").substring(0, limit);
    }
}
