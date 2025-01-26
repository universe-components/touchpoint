package com.universe.touchpoint.ai;

import com.universe.touchpoint.config.AIModelConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AIModel<T, C, R> {

    protected final T client;
    protected final AIModelConfig config;
    protected List<C> completions = new ArrayList<>();

    public AIModel(T client, AIModelConfig config) {
        this.client = client;
        this.config = config;
    }

    public R selectChoice(Map<C, List<R>> choices) {
        // 随机选择一个 choice
        List<R> choiceList = choices.values().iterator().next();
        return choiceList.get(ThreadLocalRandom.current().nextInt(choiceList.size()));
    }

    public T getClient() {
        return client;
    }

    public abstract void createCompletion(String content);
    public abstract Map<C, List<R>> predict();

}
