package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.SocketProtocol;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.Map;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<Triple<TransportConfig<?>, AIModelConfig, SocketProtocol>> {

    @Override
    public <C extends AgentContext> Triple<TransportConfig<?>, AIModelConfig, SocketProtocol> onStateChange(Object actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            AgentActionMetaInfo actionMetaInfo = (AgentActionMetaInfo) actionMeta;
            ActionGraphBuilder.getTaskGraph(task).addNode(actionMetaInfo);
            Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> graph  = ActionGraphBuilder.getTaskGraph(task).getAdjList();
            for (Map.Entry<AgentActionMetaInfo, List<AgentActionMetaInfo>> entry : graph.entrySet()) {
                if (entry.getKey().inputClassName().equals(actionMetaInfo.outputClassName())) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMetaInfo, entry.getKey());
                }
                if (entry.getKey().outputClassName().equals(actionMetaInfo.inputClassName())) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(entry.getKey(), actionMetaInfo);
                }
            }
            return Triple.of(
                    ConfigManager.selectTransport(null, task),
                    ConfigManager.selectModel(null, null, task),
                    ConfigManager.selectAgentSocketProtocol(task)
            );
        }
        return null;
    }

}
