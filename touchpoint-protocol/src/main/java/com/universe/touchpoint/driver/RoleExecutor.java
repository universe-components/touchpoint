package com.universe.touchpoint.driver;

import java.util.HashMap;
import java.util.Map;

public class RoleExecutor<Executor> {

    private final Map<String, Executor> operatorMap = new HashMap<>();

    public void registerOperator(String action, Executor actionCoordinator) {
        operatorMap.put(action, actionCoordinator);
    }

    public Executor getOperator(String action) {
        return operatorMap.get(action);
    }

}
