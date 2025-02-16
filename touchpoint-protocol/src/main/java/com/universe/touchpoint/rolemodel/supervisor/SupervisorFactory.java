package com.universe.touchpoint.rolemodel.supervisor;

import java.util.HashMap;
import java.util.Map;

public class SupervisorFactory {

    private static final Object lock = new Object();
    private static final Map<String, Supervisor> supervisorMap = new HashMap<>();

    public static Supervisor getSupervisor(String task) {
        if (!supervisorMap.containsKey(task)) {
            synchronized (lock) {
                if (!supervisorMap.containsKey(task)) {
                    supervisorMap.put(task, new Supervisor());
                }
            }
        }
        return supervisorMap.get(task);
    }

}
