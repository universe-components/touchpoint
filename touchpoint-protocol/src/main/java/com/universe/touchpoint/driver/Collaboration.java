package com.universe.touchpoint.driver;

import com.universe.touchpoint.api.ActionCoordinator;

import java.util.HashMap;
import java.util.Map;

public class Collaboration {

    private final Map<String, ActionCoordinator> operatorMap = new HashMap<>();

    public void registerOperator(String status, ActionCoordinator actionCoordinator) {
        operatorMap.put(status, actionCoordinator);
    }

    public ActionCoordinator getOperator(String status) {
        return operatorMap.get(status);
    }

}
