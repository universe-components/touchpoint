package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import org.apache.commons.lang3.tuple.Triple;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<AgentActionMetaInfo, Triple<TransportConfig<?>, AIModelConfig, ActionMetricConfig>> {

    @Override
    public <C extends AgentContext> Triple<TransportConfig<?>, AIModelConfig, ActionMetricConfig> onStateChange(AgentActionMetaInfo actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointSwapAction(actionMeta.getActionName(), actionMeta);

            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            actionMeta.getToActions().getToActions(task).forEach(toAction -> {
                if (driverRegion.containsTouchPointSwapAction(toAction)) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, driverRegion.getTouchPointSwapAction(toAction));
                }
            });
            return Triple.of(
                    ConfigManager.selectTransport(null, task),
                    ConfigManager.selectModel(null, null, task),
                    ConfigManager.selectActionMetricConfig(null, task)
            );
        }
        return null;
    }

}
