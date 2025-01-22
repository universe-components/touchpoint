package com.universe.touchpoint.arp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.Agent;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.ApkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AgentRouterManager {

    public static void registerRouteEntry(String[] toAgents, Context context) {
        String apkPath = ApkUtils.getApkPath(context);
        assert apkPath != null;
        RePluginHost.install(apkPath);

        // 发送路由更新广播
        String routerAction = TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME);

        Intent routerIntent = new Intent(routerAction);
        ArrayList<String> routeEntries = Arrays.stream(toAgents)
                .map(agent -> AgentRouter.buildChunk(agent, Agent.getProperty("name")))
                .collect(Collectors.toCollection(ArrayList::new));

        routerIntent.putStringArrayListExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME, routeEntries);

        context.sendBroadcast(routerIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void registerRouterReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME));
        context.registerReceiver(new AgentRouterBroadcastReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}
