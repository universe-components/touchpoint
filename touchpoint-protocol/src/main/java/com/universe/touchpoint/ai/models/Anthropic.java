package com.universe.touchpoint.ai.models;

import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.Completion;
import com.anthropic.models.CompletionCreateParams;
import com.anthropic.models.Model;
import com.google.common.collect.Lists;
import com.universe.touchpoint.ai.AIModel;
import com.anthropic.client.AnthropicClient;
import com.universe.touchpoint.config.ai.LangModelConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Anthropic extends AIModel<AnthropicClient, Completion, String> {

    public Anthropic(LangModelConfig modelConfig) {
        super(AnthropicOkHttpClient.builder()
                .apiKey(modelConfig.getApiKey())
                .build(), modelConfig);
    }

    @Override
    public void createCompletion(String content) {
        CompletionCreateParams params = CompletionCreateParams.builder()
                .maxTokensToSample(1024L)
                .prompt(content)
                .model((Model) Objects.requireNonNull(
                        LangModelConfig.modelConfigMap.get(config.getModel())))
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
