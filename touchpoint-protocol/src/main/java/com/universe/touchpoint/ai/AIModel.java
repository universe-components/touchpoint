package com.universe.touchpoint.ai;

import com.universe.touchpoint.config.ai.AIModelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AIModel<Client, Completion, Choice> {

    protected final Client client;
    protected final AIModelConfig config;
    protected List<Completion> completions = new ArrayList<>();

    public AIModel(Client client, AIModelConfig config) {
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

    public abstract void createCompletion(String content);
    public abstract Map<Completion, List<Choice>> predict();

}
