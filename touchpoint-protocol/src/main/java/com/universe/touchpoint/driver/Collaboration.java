package com.universe.touchpoint.driver;

import com.universe.touchpoint.api.Operator;

import java.util.HashMap;
import java.util.Map;

public class Collaboration {

    private Class<Enum<?>> statusClass;
    private final Map<String, Operator<?>> operatorMap = new HashMap<>();

    public void setStatusClass(Class<Enum<?>> statusClass) {
        this.statusClass = statusClass;
    }

    public void registerOperator(String status, Operator<?> operator) {
        operatorMap.put(status, operator);
    }

    public Class<Enum<?>> getStatusClass() {
        return statusClass;
    }

    public Operator<?> getOperator(String status) {
        return operatorMap.get(status);
    }

}
