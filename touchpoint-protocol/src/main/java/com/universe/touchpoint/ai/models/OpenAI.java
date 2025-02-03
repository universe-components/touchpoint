package com.universe.touchpoint.ai.models;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.ai.AIModel;
import com.universe.touchpoint.config.AIModelConfig;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OpenAI extends AIModel<OpenAIClient, ChatCompletion, ChatCompletion.Choice> {

    public OpenAI(AIModelConfig modelConfig) {
        super(OpenAIOkHttpClient.builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .apiKey(AgentBuilder
                        .getBuilder()
                        .getConfig()
                        .getModelConfig()
                        .getApiKey())
                .build(), modelConfig);
    }

    @Override
    public void createCompletion(String content) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(content)
                .model((ChatModel) Objects.requireNonNull(
                        AIModelConfig.modelConfigMap.get(config.getModel())))
                .temperature(config.getTemperature())
                .build();

        this.completions.add(client.chat().completions().create(params));
    }

    @Override
    public Map<ChatCompletion, List<ChatCompletion.Choice>> predict() {
        Map<ChatCompletion, List<ChatCompletion.Choice>> choices = new HashMap<>();
        for (ChatCompletion chatCompletion : completions) {
            choices.put(chatCompletion, chatCompletion.choices());
        }
        return choices;
    }

}
