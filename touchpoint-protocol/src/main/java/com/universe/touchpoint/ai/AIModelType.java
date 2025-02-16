package com.universe.touchpoint.ai;

public enum AIModelType {

    OPEN_AI("openai"),
    ANTHROPIC("claude"),
    DEEPSEEK("deepseek");

    private final String type;

    AIModelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
