package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.Model;

public class AgentActionMeta {

    private final String name;
    private final Class<? extends TouchPoint> inputClass;
    private final Model model;

    public AgentActionMeta(String name, Class<? extends TouchPoint> inputClass, Model model) {
        super();
        this.name = name;
        this.inputClass = inputClass;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public Class<? extends TouchPoint> getInputClass() {
        return inputClass;
    }

    public Model getModel() {
        return model;
    }

}
