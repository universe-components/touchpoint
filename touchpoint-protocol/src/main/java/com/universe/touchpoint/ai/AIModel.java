package com.universe.touchpoint.ai;

import com.universe.touchpoint.config.ai.LangModelConfig;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AIModel<Client, Request, Completion, Choice> {

    protected final Client client;
    protected final LangModelConfig config;
    protected Completion completionService;

    public AIModel(Client client, LangModelConfig config) {
        this.client = client;
        this.config = config;
    }

    public Choice selectChoice(Map<Completion, List<Choice>> choices) {
        // 随机选择一个 choice
        List<Choice> choiceList = choices.values().iterator().next();
        return choiceList.get(ThreadLocalRandom.current().nextInt(choiceList.size()));
    }

    public Client getClient() {
        return client;
    }

    public abstract void createCompletion();
    public abstract Choice predict(Request request);

}
