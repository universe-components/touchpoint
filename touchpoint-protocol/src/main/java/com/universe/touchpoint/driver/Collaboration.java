package com.universe.touchpoint.driver;

import com.universe.touchpoint.api.ActionCoordinator;

import java.util.HashMap;
import java.util.Map;

public class Collaboration {

    private Class<Enum<?>> statusClass;
    private final Map<String, ActionCoordinator> operatorMap = new HashMap<>();

    public void setStatusClass(Class<Enum<?>> statusClass) {
        this.statusClass = statusClass;
    }

    public void registerOperator(String status, ActionCoordinator actionCoordinator) {
        operatorMap.put(status, actionCoordinator);
    }

    public Class<Enum<?>> getStatusClass() {
        return statusClass;
    }

    public ActionCoordinator getOperator(String status) {
        return operatorMap.get(status);
    }

}
