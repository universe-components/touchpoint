package com.universe.touchpoint.router;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Pair;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.AgentReporter;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.helper.TouchPointHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AgentRouterReporter extends AgentReporter<List<Pair<String, List<Object>>>> {

    @Override
    public void report(List<Pair<String, List<Object>>> receiverFilterPair, Context context) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            for (String fromAgent : (String[]) pair.second.get(1)) {
                // 发送路由更新广播
                String routerAction = TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME, fromAgent);

                Intent routerIntent = new Intent(routerAction);
                ArrayList<String> routeEntries = Arrays.stream((String[]) pair.second.get(1))
                        .map(agent -> AgentRouter.buildChunk(agent, Agent.getName()) + "||" + pair.first)
                        .collect(Collectors.toCollection(ArrayList::new));

                routerIntent.putStringArrayListExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME, routeEntries);

                context.sendBroadcast(routerIntent);
            }
        }
    }

    @Override
    public <C extends AgentContext> void registerReceiver(Context appContext, C context) {
        ((TaskActionContext) context).getRoutes();
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME,
                        context.getBelongTask()));
        appContext.registerReceiver(new AgentRouterReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}
