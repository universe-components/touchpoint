package com.universe.touchpoint.ai.models;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionUserMessageParam;
import com.openai.models.ChatModel;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.ai.AIModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAI extends AIModel<OpenAIClient, ChatCompletion, ChatCompletion.Choice> {

    public OpenAI() {
        super(OpenAIOkHttpClient.builder()
                .apiKey(TouchPointConstants.AI_MODEL_API_KEY)
                .build());
    }

    @Override
    public void createCompletion(String content) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addMessage(ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.USER)
                        .content(content)
                        .build())
                .model(ChatModel.O1)
                .build();

        this.completions.add(client.chat().completions().create(params));
    }

    @Override
    public Map<ChatCompletion, List<ChatCompletion.Choice>> run() {
        Map<ChatCompletion, List<ChatCompletion.Choice>> choices = new HashMap<>();
        for (ChatCompletion chatCompletion : completions) {
            choices.put(chatCompletion, chatCompletion.choices());
        }
        return choices;
    }

}
