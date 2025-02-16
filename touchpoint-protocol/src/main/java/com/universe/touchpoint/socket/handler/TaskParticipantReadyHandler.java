package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import java.util.Arrays;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<AgentActionMetaInfo, Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(AgentActionMetaInfo actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointSwapAction(actionMeta.actionName(), actionMeta);

            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            Arrays.stream(actionMeta.toActions()).forEach(toAction -> {
                if (driverRegion.containsTouchPointSwapAction(toAction)) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, driverRegion.getTouchPointSwapAction(toAction));
                }
            });
            return Pair.create(
                    ConfigManager.selectTransport(null, task),
                    ConfigManager.selectModel(null, null, task)
            );
        }
        return null;
    }

}
