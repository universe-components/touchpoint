package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.context.TouchPoint;

import java.util.HashMap;
import java.util.Map;

public class CoordinatorFactory {

    private static final Object lock = new Object();
    private static final Map<String, Coordinator<?, ?>> coordinatorMap = new HashMap<>();

    public static <SocketInput extends TouchPoint, SocketOutput extends TouchPoint> Coordinator<SocketInput, SocketOutput> getCoordinator(String task) {
        if (!coordinatorMap.containsKey(task)) {
            synchronized (lock) {
                if (!coordinatorMap.containsKey(task)) {
                    coordinatorMap.put(task, new Coordinator<SocketInput, SocketOutput>());
                }
            }
        }
        return (Coordinator<SocketInput, SocketOutput>) coordinatorMap.get(task);
    }

}
