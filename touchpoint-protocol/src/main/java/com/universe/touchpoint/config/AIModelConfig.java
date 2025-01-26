package com.universe.touchpoint.config;

import com.universe.touchpoint.ai.AIModelType;

public class AIModelConfig {

    private final Model model;
    private final float temperature;
    private AIModelType type;

    public AIModelConfig(Model model, float temperature, AIModelType type) {
        this.model = model;
        this.temperature = temperature;
        this.type = type;
    }

    public AIModelConfig(Model model, float temperature) {
        this.model = model;
        this.temperature = temperature;
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

    public float getTemperature() {
        return temperature;
    }

    public AIModelType getType() {
        return type;
    }

}
