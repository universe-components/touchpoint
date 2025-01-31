package com.universe.touchpoint.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.AgentBroadcaster;
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

public class AIModelBroadcaster extends AgentBroadcaster<AIModelConfig> {

    @Override
    public void send(AIModelConfig config, Context context) {
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

    public static class AIModelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] aiModelConfig = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_AI_MODEL_CONFIG_EVENT_NAME);

            AIModelConfig config = SerializeUtils.deserializeFromByteArray(aiModelConfig, AIModelConfig.class);
            if (config != null) {
                AgentBuilder.getBuilder().getConfig().setModelConfig(config);
            }
        }

    }

}
