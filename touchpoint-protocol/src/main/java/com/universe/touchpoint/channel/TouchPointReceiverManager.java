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
    public void registerTouchPointReceiver(Context appContext, String name, String[] filters, AgentActionMeta agentActionMeta) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(agentActionMeta.name());
            TouchPointListener<?, ?> tpInstanceReceiver = (TouchPointListener<?, ?>) tpInstanceReceiverClass.getConstructor().newInstance();

            registerDefaultOrActionReceiver(appContext, filters, agentActionMeta.inputClass());
            registerAgentFinishReceiver(appContext, filters, agentActionMeta.inputClass());

            registerContextReceiver(name, filters, tpInstanceReceiver);
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerDefaultOrActionReceiver(Context appContext, String[] filters, Class<? extends TouchPoint> touchPointClass) {
        TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

        IntentFilter intentFilter = new IntentFilter();
        for (String filter : filters) {
            String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                    filter, Agent.getName()
            ));
            intentFilter.addAction(filterAction);
        }
        appContext.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerAgentFinishReceiver(Context appContext, String[] filters, Class<? extends TouchPoint> touchPointClass) {
        TouchPointBroadcastReceiver<? extends TouchPoint> agentFinishReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

        IntentFilter agentFinishFilter = new IntentFilter();
        for (String filter : filters) {
            String agentFinishAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                    Agent.getName(), filter
            ));
            agentFinishFilter.addAction(agentFinishAction);
        }
        appContext.registerReceiver(agentFinishReceiver, agentFinishFilter, Context.RECEIVER_EXPORTED);
    }

    public void registerContextReceiver(String name, String[] filters, TouchPointListener<?, ?> tpInstanceReceiver) {
        String ctxName = TouchPointHelper.touchPointPluginName(name);
        for (String filter : filters) {
            String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                    filter, Agent.getName()
            ));
            TouchPointContextManager.getContext(ctxName).putTouchPointReceiver(filterAction, tpInstanceReceiver);
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
