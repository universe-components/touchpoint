package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import java.util.List;
import java.util.Map;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<AgentActionMetaInfo, Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(AgentActionMetaInfo actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> graph  = ActionGraphBuilder.getTaskGraph(task).getAdjList();
            for (Map.Entry<AgentActionMetaInfo, List<AgentActionMetaInfo>> entry : graph.entrySet()) {
                if (entry.getKey().inputClassName().equals(actionMeta.outputClassName())) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, entry.getKey());
                }
                if (entry.getKey().outputClassName().equals(actionMeta.inputClassName())) {
                    if (actionMeta.role() == ActionRole.SUPERVISOR) {
                        ActionGraphBuilder.getTaskGraph(task).addEdgeAtStart(entry.getKey(), actionMeta);
                    } else {
                        ActionGraphBuilder.getTaskGraph(task).addEdge(entry.getKey(), actionMeta);
                    }
                }
            }
            return Pair.create(
                    ConfigManager.selectTransport(null, task),
                    ConfigManager.selectModel(null, null, task)
            );
        }
        return null;
    }

}
