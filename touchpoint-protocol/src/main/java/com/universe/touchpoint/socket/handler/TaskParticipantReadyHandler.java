package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import java.util.Map;

public class TaskParticipantReadyHandler<Config> implements AgentSocketStateHandler<AgentActionMetaInfo, Map<String, Config>> {

    @Override
    public <C extends AgentContext> Map<String, Config> onStateChange(AgentActionMetaInfo actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointSwapAction(actionMeta.getActionName(), actionMeta);

            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            actionMeta.getToActions().getToActions(task).forEach(toAction -> {
                if (driverRegion.containsTouchPointSwapAction(toAction)) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, driverRegion.getTouchPointSwapAction(toAction));
                }
            });
            return (Map<String, Config>) Map.of(
                    "transport", ConfigManager.selectTransport(null, task),
                    "langmodel", ConfigManager.selectModel(null, null, task),
                    "visionmodel", ConfigManager.selectVisionModel(null, null, task),
                    "visionLangModel", ConfigManager.selectVisionLangModel(null, null, task),
                    "actionMetric", ConfigManager.selectActionMetricConfig(null, task),
                    "taskMetric", ConfigManager.selectTaskMetricConfig(task)
            );
        }
        return null;
    }

}
