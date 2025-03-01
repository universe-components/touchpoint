package com.universe.touchpoint.ai.models;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import com.openai.services.blocking.chat.CompletionService;
import com.universe.touchpoint.ai.AIModel;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.Model;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAI extends AIModel<OpenAIClient, String, CompletionService, Map<ChatCompletion, List<ChatCompletion.Choice>>> {

    static {
        modelConfigMap.put(Model.GPT_3_5, ChatModel.GPT_3_5_TURBO);
        modelConfigMap.put(Model.GPT_4, ChatModel.GPT_4);
        modelConfigMap.put(Model.o1, ChatModel.O1);
    }

    public OpenAI(LangModelConfig modelConfig) {
        super(OpenAIOkHttpClient.builder()
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .apiKey(modelConfig.getApiKey())
                .build(), modelConfig);
    }

    @Override
    public void createCompletion() {
        this.completionService = client.chat().completions();
    }

    @Override
    public Map<ChatCompletion, List<ChatCompletion.Choice>> predict(String content) {
        Map<ChatCompletion, List<ChatCompletion.Choice>> choices = new HashMap<>();
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(content)
                .model((ChatModel) modelConfigMap.get(config.getModel()))
                .temperature(config.getTemperature())
                .build();
        ChatCompletion chatCompletion = completionService.create(params);
        choices.put(chatCompletion, chatCompletion.choices());
        return choices;
    }

}
