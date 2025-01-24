package com.universe.touchpoint;

public class Action {

    private final String name;
    private final Class<? extends TouchPoint> inputClass;

    public Action(String name, Class<? extends TouchPoint> inputClass) {
        super();
        this.name = name;
        this.inputClass = inputClass;
    }

    public String getName() {
        return name;
    }

    public Class<? extends TouchPoint> getInputClass() {
        return inputClass;
    }

}
