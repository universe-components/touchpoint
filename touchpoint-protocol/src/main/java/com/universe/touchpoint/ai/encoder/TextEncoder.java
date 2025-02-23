package com.universe.touchpoint.ai.encoder;

import com.openai.client.OpenAIClient;
import com.openai.models.EmbeddingCreateParams;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.MediaEncoder;
import com.universe.touchpoint.config.ai.LangModelConfig;

import java.util.List;

public class TextEncoder implements MediaEncoder<String, List<Double>, LangModelConfig> {

    @Override
    public List<Double> run(String text, LangModelConfig modelConfig) {
        OpenAIClient client = (OpenAIClient) AIModelFactory.getModel(modelConfig).getClient();
        EmbeddingCreateParams embeddingCreateParams = EmbeddingCreateParams.builder().input(text).model(modelConfig.getModel().name()).build();
        return client.embeddings().create(embeddingCreateParams).data().get(0).embedding();
    }

}
