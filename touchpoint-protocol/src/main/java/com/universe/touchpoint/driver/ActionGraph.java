package com.universe.touchpoint.driver;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.ActionConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActionGraph {

    // 图的缓存结构：将节点映射为后置节点列表
    private final Map<String, List<ActionConfig>> graph = new HashMap<>();
    private final Map<String, ActionConfig> actionConfigCache = new HashMap<>();

    // 单例实例
    private static ActionGraph instance;

    // 锁对象
    private static final Object lock = new Object();

    // 获取单例实例
    public static ActionGraph getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ActionGraph();
                }
            }
        }
        return instance;
    }

    // 添加ActionConfig到图中
    public void addActionConfig(ActionConfig actionConfig) {
        String name = actionConfig.getName();

        // 缓存ActionConfig对象
        actionConfigCache.put(name, actionConfig);

        // 获取所有后置节点，合并toAgents和toActions
        Set<String> toNodes = actionConfig.getAllSuccessors();

        // 为每个后置节点添加到图中
        for (String node : toNodes) {
            // 获取当前后置节点的ActionConfig对象
            List<ActionConfig> predecessors = graph.computeIfAbsent(node, k -> new ArrayList<>());
            predecessors.add(actionConfig);
        }
    }

    // 获取一个节点的后置节点（合并toAgents和toActions）
    public List<ActionConfig> getSuccessors(String name) {
        List<ActionConfig> successors = graph.getOrDefault(name, Collections.emptyList());
        assert successors != null;
        return successors.isEmpty() ? graph.getOrDefault(Agent.getName(), Collections.emptyList()) : successors;
    }

    // 获取图中的所有节点
    public Collection<ActionConfig> getAllNodes() {
        return actionConfigCache.values();
    }

    // 清除图和缓存
    public void clear() {
        graph.clear();
        actionConfigCache.clear();
    }

}
