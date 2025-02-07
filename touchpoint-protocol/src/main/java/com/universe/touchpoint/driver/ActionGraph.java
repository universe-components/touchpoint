package com.universe.touchpoint.driver;

import com.universe.touchpoint.config.ActionConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActionGraph {

    // 使用 HashMap 存储邻接表，键为 ActionConfig 节点，值为与该节点相邻的节点列表
    private final Map<ActionConfig, List<ActionConfig>> adjList = new HashMap<>();

    // 根据指定节点获取该节点的子图（包含该节点以及从该节点可达的所有节点和边）

    // 添加节点：如果图中不存在该节点，则添加一个新的条目
    public void addNode(ActionConfig node) {
        if (!adjList.containsKey(node)) {
            adjList.put(node, new ArrayList<>());
        }
    }

    // 添加边（有向边）：从 from 到 to
    public void addEdge(ActionConfig from, ActionConfig to) {
        // 如果节点不存在，则先添加节点
        addNode(from);
        addNode(to);
        Objects.requireNonNull(adjList.get(from)).add(to);
    }

    // 获取指定节点的所有前置节点
    public List<ActionConfig> getPredecessors(ActionConfig node) {
        List<ActionConfig> predecessors = new ArrayList<>();
        for (Map.Entry<ActionConfig, List<ActionConfig>> entry : adjList.entrySet()) {
            if (entry.getValue().contains(node)) {
                predecessors.add(entry.getKey());
            }
        }
        return predecessors;
    }

    // 获取指定节点的后置节点
    public List<ActionConfig> getSuccessors(ActionConfig node) {
        return adjList.getOrDefault(node, Collections.emptyList());
    }

    public void updateNodeDesc(ActionConfig node) {
        for (ActionConfig key : adjList.keySet()) {
            if (key.getName().equals(node.getName())) {
                if (node.getDesc() != null && (key.getDesc() == null || key.getDesc().isEmpty())) {
                    key.setDesc(node.getDesc());
                }
                return;
            }
        }
        // 如果图中不存在该节点，则添加节点
        addNode(node);
    }

    // 删除节点：同时需要将该节点从所有邻接列表中移除
    public void removeNode(ActionConfig node) {
        if (adjList.containsKey(node)) {
            // 移除其他节点中与该节点相连的边
            for (List<ActionConfig> neighbors : adjList.values()) {
                neighbors.remove(node);
            }
            // 移除该节点
            adjList.remove(node);
        }
    }

    // 删除边：从 from 节点的邻接列表中移除 to 节点
    public void removeEdge(ActionConfig from, ActionConfig to) {
        if (adjList.containsKey(from)) {
            Objects.requireNonNull(adjList.get(from)).remove(to);
        }
    }

    // 获取某个节点的所有邻接节点
    public List<ActionConfig> getNeighbors(ActionConfig node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    public Map<ActionConfig, List<ActionConfig>> getAdjList() {
        return adjList;
    }

}
