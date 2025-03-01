package com.universe.touchpoint.ai.models;

import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.Completion;
import com.anthropic.models.CompletionCreateParams;
import com.anthropic.models.Model;
import com.anthropic.services.blocking.CompletionService;
import com.universe.touchpoint.ai.AIModel;
import com.anthropic.client.AnthropicClient;
import com.universe.touchpoint.config.ai.LangModelConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Anthropic extends AIModel<AnthropicClient, String, CompletionService, Map<Completion, String>> {

    static {
        modelConfigMap.put(com.universe.touchpoint.config.ai.Model.ClAUDE_3_5_SONNET, com.anthropic.models.Model.CLAUDE_3_5_SONNET_LATEST);
    }

    public Anthropic(LangModelConfig modelConfig) {
        super(AnthropicOkHttpClient.builder()
                .apiKey(modelConfig.getApiKey())
                .build(), modelConfig);
    }

    @Override
    public void createCompletion() {
        this.completionService = client.completions();
    }

    @Override
    public Map<Completion, String> predict(String content) {
        Map<Completion, String> choices = new HashMap<>();
        CompletionCreateParams params = CompletionCreateParams.builder()
                .maxTokensToSample(1024L)
                .prompt(content)
                .model((Model) Objects.requireNonNull(modelConfigMap.get(config.getModel())))
                .temperature(config.getTemperature())
                .build();
        Completion completion = completionService.create(params);
        choices.put(completion, completion.completion());
        return choices;
    }

}
