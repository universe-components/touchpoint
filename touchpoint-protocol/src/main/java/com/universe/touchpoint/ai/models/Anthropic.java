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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Anthropic extends AIModel<AnthropicClient, Completion, String> {

    public Anthropic() {
        super(AnthropicOkHttpClient.builder()
                .apiKey(AgentBuilder
                        .getBuilder()
                        .getConfig()
                        .getModelApiKey())
                .build());
    }

    @Override
    public void createCompletion(String content) {
        CompletionCreateParams params = CompletionCreateParams.builder()
                .maxTokensToSample(1024L)
                .prompt(content)
                .model((Model) Objects.requireNonNull(
                        AgentConfig.modelConfigMap.get(
                                AgentBuilder.getBuilder().getConfig().getModel())))
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
