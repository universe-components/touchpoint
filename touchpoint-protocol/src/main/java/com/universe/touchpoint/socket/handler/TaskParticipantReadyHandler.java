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

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(Object actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            AgentActionMetaInfo actionMetaInfo = (AgentActionMetaInfo) actionMeta;
            ActionGraphBuilder.getTaskGraph(task).addNode(actionMetaInfo);
            Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> graph  = ActionGraphBuilder.getTaskGraph(task).getAdjList();
            for (Map.Entry<AgentActionMetaInfo, List<AgentActionMetaInfo>> entry : graph.entrySet()) {
                if (entry.getKey().inputClassName().equals(actionMetaInfo.outputClassName())) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMetaInfo, entry.getKey());
                }
                if (entry.getKey().outputClassName().equals(actionMetaInfo.inputClassName())) {
                    if (actionMetaInfo.role() == ActionRole.SUPERVISOR) {
                        ActionGraphBuilder.getTaskGraph(task).addEdgeAtStart(entry.getKey(), actionMetaInfo);
                    } else {
                        ActionGraphBuilder.getTaskGraph(task).addEdge(entry.getKey(), actionMetaInfo);
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
