package com.universe.touchpoint;

import com.openai.models.ChatModel;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.config.Transport;

import java.util.HashMap;
import java.util.Map;

public class AgentConfig {

    private final ModelConfig modelConfig = new ModelConfig();
    private Transport transportType;

    public ModelConfig getModelConfig() {
        return modelConfig;
    }

    public Transport getTransportType() {
        return transportType;
    }

    public void setTransportType(Transport transportType) {
        this.transportType = transportType;
    }

    public static class ModelConfig {
        private Model model;
        private Float temperature;
        private String modelApiKey;

        public static final Map<Model, Object> modelConfigMap = new HashMap<>();
        static {
            modelConfigMap.put(Model.GPT_3_5, ChatModel.GPT_3_5_TURBO);
            modelConfigMap.put(Model.GPT_4, ChatModel.GPT_4);
            modelConfigMap.put(Model.o1, ChatModel.O1);
            modelConfigMap.put(Model.ClAUDE_3_5_SONNET, com.anthropic.models.Model.CLAUDE_3_5_SONNET_LATEST);
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public Float getTemperature() {
            return temperature;
        }

        public void setTemperature(Float temperature) {
            this.temperature = temperature;
        }

        public String getModelApiKey() {
            return modelApiKey;
        }

        public void setModelApiKey(String modelApiKey) {
            this.modelApiKey = modelApiKey;
        }
    }

}
