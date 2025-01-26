package com.universe.touchpoint.ai.models;

import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.Completion;
import com.anthropic.models.CompletionCreateParams;
import com.anthropic.models.Model;
import com.google.common.collect.Lists;
import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.AgentConfig;
import com.universe.touchpoint.ai.AIModel;
import com.anthropic.client.AnthropicClient;
import com.universe.touchpoint.config.AIModelConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Anthropic extends AIModel<AnthropicClient, Completion, String> {

    public Anthropic(AIModelConfig modelConfig) {
        super(AnthropicOkHttpClient.builder()
                .apiKey(AgentBuilder
                        .getBuilder()
                        .getConfig()
                        .getModelConfig()
                        .getModelApiKey())
                .build(), modelConfig);
    }

    @Override
    public void createCompletion(String content) {
        CompletionCreateParams params = CompletionCreateParams.builder()
                .maxTokensToSample(1024L)
                .prompt(content)
                .model((Model) Objects.requireNonNull(
                        AgentConfig.ModelConfig.modelConfigMap.get(config.getModel())))
                .temperature(config.getTemperature())
                .build();
        this.completions.add(client.completions().create(params));
    }

    @Override
    public Map<Completion, List<String>> predict() {
        Map<Completion, List<String>> choices = new HashMap<>();
        for (Completion completion : completions) {
            choices.put(completion, Lists.newArrayList(completion.completion()));
        }
        return choices;
    }

}
