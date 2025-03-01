package com.universe.touchpoint.config.ai;

import com.universe.touchpoint.ai.AIModelType;

public class LangModelConfig {

    private Model model;
    private float temperature;
    private String apiKey;
    private String apiHost;
    private AIModelType type;

    public LangModelConfig() {}

    public LangModelConfig(Model model, float temperature, AIModelType type) {
        this(model, temperature, null, type);
    }

    public LangModelConfig(Model model, float temperature, String apiHost, AIModelType type) {
        this.model = model;
        this.temperature = temperature;
        this.apiHost = apiHost;
        this.type = type;
    }

    public LangModelConfig(Model model, float temperature) {
        this(model, temperature, (String) null);
    }

    public LangModelConfig(Model model, float temperature, String apiKey) {
        this.model = model;
        this.temperature = temperature;
        this.apiKey = apiKey;
        switch (model) {
            case GPT_3_5, o1, GPT_4, NONE -> this.type = AIModelType.OPEN_AI;
            case ClAUDE_3_5_SONNET -> this.type = AIModelType.ANTHROPIC;
            case OPEN_VLA -> this.type = AIModelType.OPEN_VLA;
        }
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getApiHost() {
        return apiHost;
    }

    public AIModelType getType() {
        return type;
    }

}
