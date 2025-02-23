package com.universe.touchpoint.config.ai;

import com.openai.models.ChatModel;
import com.universe.touchpoint.ai.AIModelType;

import java.util.HashMap;
import java.util.Map;

public class LangModelConfig {

    private Model model;
    private float temperature;
    private String apiKey;
    private AIModelType type;

    public static final Map<Model, Object> modelConfigMap = new HashMap<>();
    static {
        modelConfigMap.put(Model.GPT_3_5, ChatModel.GPT_3_5_TURBO);
        modelConfigMap.put(Model.GPT_4, ChatModel.GPT_4);
        modelConfigMap.put(Model.o1, ChatModel.O1);
        modelConfigMap.put(Model.ClAUDE_3_5_SONNET, com.anthropic.models.Model.CLAUDE_3_5_SONNET_LATEST);
    }

    public LangModelConfig() {}

    public LangModelConfig(Model model, float temperature, AIModelType type) {
        this.model = model;
        this.temperature = temperature;
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
            case GPT_3_5, o1, GPT_4, NONE ->
                    this.type = AIModelType.OPEN_AI;
            case ClAUDE_3_5_SONNET ->
                    this.type = AIModelType.ANTHROPIC;
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

    public AIModelType getType() {
        return type;
    }

}
