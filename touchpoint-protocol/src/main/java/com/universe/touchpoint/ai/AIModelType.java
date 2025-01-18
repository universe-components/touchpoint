package com.universe.touchpoint.ai;

public enum AIModelType {

    OPEN_AI("openai"),
    ANTHROPIC("claude"),
    ;

    private final String type;

    AIModelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
