package com.universe.touchpoint.plan;

import com.universe.touchpoint.agent.AgentActionMetaInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ActionGraph {

    private String name = "default";
    // 使用 HashMap 存储邻接表，键为 AgentActionMetaInfo 节点，值为与该节点相邻的节点列表
    private final Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> adjList = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AgentActionMetaInfo> getFirstNodes() {
        // 用一个集合存储所有节点
        Set<AgentActionMetaInfo> allNodes = adjList.keySet();
        // 用一个集合存储所有被作为邻接点的节点
        Set<AgentActionMetaInfo> nodesWithPredecessors = new HashSet<>();

        // 遍历邻接表，找出所有作为邻接节点的节点
        for (List<AgentActionMetaInfo> neighbors : adjList.values()) {
            nodesWithPredecessors.addAll(neighbors);
        }

        // 返回那些在 allNodes 中，但不在 nodesWithPredecessors 中的节点
        List<AgentActionMetaInfo> result = new ArrayList<>();
        for (AgentActionMetaInfo node : allNodes) {
            if (!nodesWithPredecessors.contains(node)) {
                result.add(node);
            }
        }

        return result;
    }

    // 添加节点：如果图中不存在该节点，则添加一个新的条目
    public void addNode(AgentActionMetaInfo node) {
        if (!adjList.containsKey(node)) {
            adjList.put(node, new ArrayList<>());
        }
    }

    // 添加边（有向边）：从 from 到 to
    public void addEdge(AgentActionMetaInfo from, AgentActionMetaInfo to) {
        // 如果节点不存在，则先添加节点
        addNode(from);
        addNode(to);
        Objects.requireNonNull(adjList.get(from)).add(to);
    }

    public void addEdgeAtStart(AgentActionMetaInfo from, AgentActionMetaInfo to) {
        // 如果节点不存在，则先添加节点
        addNode(from);
        addNode(to);

        // 获取from对应的列表并将to添加到列表的第一位
        List<AgentActionMetaInfo> fromList = adjList.get(from);
        if (fromList != null) {
            fromList.add(0, to);  // 将to添加到列表的第一位
        }
    }

    public AgentActionMetaInfo getNode(AgentActionMetaInfo node) {
        if (adjList.containsKey(node)) {
            // 返回节点本身
            return node;
        }
        return null; // 如果节点不存在，则返回 null
    }

    // 获取指定节点的所有前置节点
    public List<AgentActionMetaInfo> getPredecessors(AgentActionMetaInfo node) {
        List<AgentActionMetaInfo> predecessors = new ArrayList<>();
        for (Map.Entry<AgentActionMetaInfo, List<AgentActionMetaInfo>> entry : adjList.entrySet()) {
            if (entry.getValue().contains(node)) {
                predecessors.add(entry.getKey());
            }
        }
        return predecessors;
    }

    // 获取指定节点的后置节点
    public List<AgentActionMetaInfo> getSuccessors(AgentActionMetaInfo node) {
        return adjList.getOrDefault(node, Collections.emptyList());
    }

    // 删除节点：同时需要将该节点从所有邻接列表中移除
    public void removeNode(AgentActionMetaInfo node) {
        if (adjList.containsKey(node)) {
            // 移除其他节点中与该节点相连的边
            for (List<AgentActionMetaInfo> neighbors : adjList.values()) {
                neighbors.remove(node);
            }
            // 移除该节点
            adjList.remove(node);
        }
    }

    // 删除边：从 from 节点的邻接列表中移除 to 节点
    public void removeEdge(AgentActionMetaInfo from, AgentActionMetaInfo to) {
        if (adjList.containsKey(from)) {
            Objects.requireNonNull(adjList.get(from)).remove(to);
        }
    }

    // 获取某个节点的所有邻接节点
    public List<AgentActionMetaInfo> getNeighbors(AgentActionMetaInfo node) {
        return adjList.getOrDefault(node, new ArrayList<>());
    }

    public Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> getAdjList() {
        return adjList;
    }

}
