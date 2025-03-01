package com.universe.touchpoint.ai;

public enum AIModelType {

    OPEN_AI("openai"),
    ANTHROPIC("claude"),
    DEEPSEEK("deepseek"),
    OPEN_VLA("openvla/openvla-7b");

    private final String type;

    AIModelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
