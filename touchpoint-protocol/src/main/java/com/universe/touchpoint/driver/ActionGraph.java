package com.universe.touchpoint.driver;

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
    private final Map<String, List<String>> graph = new HashMap<>();
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
    public void addActionConfig(ActionConfig actionConfig, String root) {
        String name = actionConfig.getName();

        // 缓存ActionConfig对象
        actionConfigCache.put(name, actionConfig);

        if (!containsActionConfig(name)) {
            List<String> predecessors = graph.computeIfAbsent(root, k -> new ArrayList<>());
            predecessors.add(name);
        } else {
            List<String> predecessors = graph.get(root);
            assert predecessors!= null;
            predecessors.remove(actionConfig.getName()); // 删除之前的后置节点
        }

        // 获取所有后置节点，合并toAgents和toActions
        Set<String> toNodes = actionConfig.getAllSuccessors();

        List<String> predecessors = graph.computeIfAbsent(name, k -> new ArrayList<>());
        predecessors.addAll(toNodes);
    }

    // 获取指定节点的所有前置节点
    public Map<String, List<String>> getSubGraph(String nodeName) {
        Map<String, List<String>> subGraph = new HashMap<>();
        // 递归获取后置节点
        populateSubGraph(nodeName, subGraph);
        return subGraph;
    }

    // 递归填充子图
    private void populateSubGraph(String nodeName, Map<String, List<String>> subGraph) {
        // 获取当前节点的后置节点
        List<String> successors = graph.get(nodeName);
        if (successors != null) {
            // 遍历当前节点的所有后置节点
            for (String successor : successors) {
                // 如果当前后置节点不在子图中，添加它
                if (!subGraph.containsKey(successor)) {
                    // 获取当前后置节点的后置节点
                    List<String> successorList = graph.get(successor);
                    subGraph.put(successor, successorList);

                    // 递归获取当前后置节点的后续节点
                    populateSubGraph(successor, subGraph);
                }
            }
        }
    }

    public boolean containsActionConfig(String actionName) {
        for (List<String> successors : graph.values()) {
            if (successors.contains(actionName)) {
                return true;
            }
        }
        return false;
    }

    // 获取一个节点的后置节点（合并toAgents和toActions）
    public List<String> getSuccessors(String name) {
        return graph.getOrDefault(name, Collections.emptyList());
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
