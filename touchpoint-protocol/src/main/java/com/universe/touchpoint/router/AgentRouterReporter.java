package com.universe.touchpoint.router;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.ActionReporter;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.ApkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AgentRouterReporter extends ActionReporter<String[]> {

    @Override
    public void report(String[] fromAgents, Context context) {
        String apkPath = ApkUtils.getApkPath(context);
        assert apkPath != null;
        RePluginHost.install(apkPath);

        // 发送路由更新广播
        for (String toAgent : fromAgents) {
            String routerAction = TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME, toAgent);

            Intent routerIntent = new Intent(routerAction);
            ArrayList<String> routeEntries = Arrays.stream(fromAgents)
                    .map(agent -> AgentRouter.buildChunk(agent, Agent.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            routerIntent.putStringArrayListExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME, routeEntries);

            context.sendBroadcast(routerIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME,
                        Agent.getName()));
        context.registerReceiver(new AgentRouterReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}
