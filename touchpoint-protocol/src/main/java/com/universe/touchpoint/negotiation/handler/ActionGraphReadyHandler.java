package com.universe.touchpoint.negotiation.handler;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import java.util.Map;

public class ActionGraphReadyHandler<Config> implements AgentSocketStateHandler<AgentActionMeta, Map<String, Config>> {

    @Override
    public <C extends AgentContext> Map<String, Config> onStateChange(AgentActionMeta actionMeta, C agentContext, String task) {
        if (actionMeta != null) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            metaRegion.putTouchPointSwapAction(actionMeta.getName(), actionMeta);

            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            actionMeta.getToActions().getToActions(task).forEach(toAction -> {
                if (metaRegion.containsTouchPointSwapAction(toAction)) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, metaRegion.getTouchPointSwapAction(toAction));
                }
            });
            return (Map<String, Config>) Map.of(
                    "transport", ConfigManager.selectTransport(null, task),
                    "langmodel", ConfigManager.selectModel(null, null, task),
                    "vis5555555555ionmodel", ConfigManager.selectVisionModel(null, null, task),
                    "visionLangModel", ConfigManager.selectVisionLangModel(null, null, task),
                    "actionMetric", ConfigManager.selectActionMetricConfig(null, task),
                    "taskMetric", ConfigManager.selectTaskMetricConfig(task)
            );
        }
        return null;
    }

}
