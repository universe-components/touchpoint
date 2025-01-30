package com.universe.touchpoint.driver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.universe.touchpoint.TouchPointRegistry;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.RouteRegion;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.List;

public class AIModelRegistry implements TouchPointRegistry<AIModelConfig> {

    @Override
    public void register(AIModelConfig config, Context context) {
        RouteRegion routeRegion = TouchPointMemory.getRegion(Region.ROUTE);
        List<AgentRouteEntry> routeEntries = routeRegion.getRouteItems(Agent.getName());

        for (AgentRouteEntry entry : routeEntries) {
            String aiModelConfigAction = TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_AI_MODEL_CONFIG_FILTER_NAME,
                    entry.getToAgent().getName());

            Intent aiModelConfigIntent = new Intent(aiModelConfigAction);
            aiModelConfigIntent.putExtra(TouchPointConstants.TOUCH_POINT_AI_MODEL_CONFIG_EVENT_NAME,
                    SerializeUtils.serializeToByteArray(config));

            context.sendBroadcast(aiModelConfigIntent);
        }
    }

    @Override
    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_AI_MODEL_CONFIG_FILTER_NAME,
                        Agent.getName()));
        context.registerReceiver(new AIModelReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}
