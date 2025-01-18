package com.universe.touchpoint.ai.models;

import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.Completion;
import com.anthropic.models.CompletionCreateParams;
import com.anthropic.models.Model;
import com.google.common.collect.Lists;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.ai.AIModel;
import com.anthropic.client.AnthropicClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anthropic extends AIModel<AnthropicClient, Completion, String> {

    public Anthropic() {
        super(AnthropicOkHttpClient.builder()
                .apiKey(TouchPointConstants.AI_MODEL_API_KEY)
                .build());
    }

    @Override
    public void createCompletion(String content) {
        CompletionCreateParams params = CompletionCreateParams.builder()
                .maxTokensToSample(1024L)
                .prompt(content)
                .model(Model.CLAUDE_3_5_SONNET_LATEST)
                .build();
        this.completions.add(client.completions().create(params));
    }

    @Override
    public Map<Completion, List<String>> run() {
        Map<Completion, List<String>> choices = new HashMap<>();
        for (Completion completion : completions) {
            choices.put(completion, Lists.newArrayList(completion.completion()));
        }
        return choices;
    }

}
