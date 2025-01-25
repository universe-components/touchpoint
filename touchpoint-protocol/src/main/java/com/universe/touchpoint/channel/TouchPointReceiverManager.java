package com.universe.touchpoint.channel;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.agent.AgentActionMeta;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.channel.broadcast.TouchPointBroadcastReceiver;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.router.AgentRouter;

import java.lang.reflect.ParameterizedType;

public class TouchPointReceiverManager {

    private static final Object mLock = new Object();
    private static TouchPointReceiverManager mInstance;

    public static TouchPointReceiverManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new TouchPointReceiverManager();
            }
            return mInstance;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerTouchPointReceiver(Context appContext, String name,
                                           String[] agentFilters, String[] actionFilters, AgentActionMeta agentActionMeta) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(agentActionMeta.name());
            TouchPointListener<?, ?> tpInstanceReceiver = (TouchPointListener<?, ?>) tpInstanceReceiverClass.getConstructor().newInstance();

            registerDefaultOrActionReceiver(appContext, agentFilters, actionFilters, agentActionMeta.inputClass());
            registerAgentFinishReceiver(appContext, agentFilters, actionFilters, agentActionMeta.inputClass());

            registerContextReceiver(name, agentFilters, actionFilters, tpInstanceReceiver);
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerDefaultOrActionReceiver(Context appContext,
                                                String[] agentFilters, String[] actionFilters, Class<? extends TouchPoint> touchPointClass) {
        TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

        IntentFilter intentFilter = new IntentFilter();
        if (agentFilters != null) {
            for (String filter : agentFilters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                intentFilter.addAction(filterAction);
            }
        }
        if (actionFilters != null) {
            for (String filter : actionFilters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                intentFilter.addAction(filterAction);
            }
        }
        appContext.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerAgentFinishReceiver(Context appContext,
                                            String[] agentFilters, String[] actionFilters, Class<? extends TouchPoint> touchPointClass) {
        TouchPointBroadcastReceiver<? extends TouchPoint> agentFinishReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

        IntentFilter agentFinishFilter = new IntentFilter();
        if (agentFilters != null) {
            for (String filter : agentFilters) {
                String agentFinishAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        Agent.getName(), filter
                ));
                agentFinishFilter.addAction(agentFinishAction);
            }
        }
        if (actionFilters != null) {
            for (String filter : actionFilters) {
                String agentFinishAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        Agent.getName(), filter
                ));
                agentFinishFilter.addAction(agentFinishAction);
            }
        }
        appContext.registerReceiver(agentFinishReceiver, agentFinishFilter, Context.RECEIVER_EXPORTED);
    }

    public void registerContextReceiver(String name, String[] agentFilters, String[] actionFilters, TouchPointListener<?, ?> tpInstanceReceiver) {
        String ctxName = TouchPointHelper.touchPointPluginName(name);
        if (agentFilters != null) {
            for (String filter : agentFilters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                TouchPointContextManager.getContext(ctxName).putTouchPointReceiver(filterAction, tpInstanceReceiver);
            }
        }
        if (actionFilters != null) {
            for (String filter : actionFilters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                TouchPointContextManager.getContext(ctxName).putTouchPointReceiver(filterAction, tpInstanceReceiver);
            }
        }
    }

    public String getReceiverType(String tpReceiverClassName) {
        String receiverType = "";
        try {
            // 通过类名获取 Class 对象
            Class<?> clazz = Class.forName(tpReceiverClassName);
            // 获取类实现的接口
            ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericInterfaces()[0];
            java.lang.reflect.Type actualParamType = parameterizedType.getActualTypeArguments()[0];

            // 输出父类的名字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                receiverType = actualParamType.getTypeName().substring(
                        actualParamType.getTypeName().lastIndexOf('.') + 1
                );
            }
        } catch (Exception e) {
            if (LogDebug.LOG){
                e.printStackTrace();
            }
        }
        return receiverType;
    }

}
