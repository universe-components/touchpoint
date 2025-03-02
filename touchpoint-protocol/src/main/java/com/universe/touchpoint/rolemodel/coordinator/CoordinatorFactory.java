package com.universe.touchpoint.rolemodel.coordinator;

import java.util.HashMap;
import java.util.Map;

public class CoordinatorFactory {

    private static final Object lock = new Object();
    private static final Map<String, Coordinator> coordinatorMap = new HashMap<>();

    public static Coordinator getCoordinator(String task) {
        if (!coordinatorMap.containsKey(task)) {
            synchronized (lock) {
                if (!coordinatorMap.containsKey(task)) {
                    coordinatorMap.put(task, new Coordinator());
                }
            }
        }
        return coordinatorMap.get(task);
    }

}
