package com.universe.touchpoint.config.ai;

public class VisionModelConfig {

    private Model model;
    private float temperature;

    public VisionModelConfig() {}

    public VisionModelConfig(Model model, float temperature) {
        this.model = model;
        this.temperature = temperature;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

}
