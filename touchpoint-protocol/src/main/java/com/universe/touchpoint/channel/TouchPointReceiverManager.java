package com.universe.touchpoint.channel;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.Agent;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.TouchPointListener;
import com.universe.touchpoint.channel.broadcast.TouchPointBroadcastReceiver;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.arp.AgentRouter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
    public void registerTouchPointReceiver(Context appContext, String name, String filter, String receiverClassName) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(receiverClassName);
            TouchPointListener<?> tpInstanceReceiver = (TouchPointListener<?>) tpInstanceReceiverClass.getConstructor().newInstance();

            Type[] interfaces = tpInstanceReceiverClass.getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            Type actualType = parameterizedType.getActualTypeArguments()[0];

            Class<?> touchPointClazz = Class.forName(actualType.getTypeName());
            Class<? extends TouchPoint> touchPointClass = touchPointClazz.asSubclass(TouchPoint.class);
            TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

            String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                    filter, Agent.getProperty("name")
            ));
            IntentFilter intentFilter = new IntentFilter(filterAction);
            appContext.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);

            TouchPointBroadcastReceiver<? extends TouchPoint> agentFinishReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);
            String agentFinishAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                    Agent.getProperty("name"), filter
            ));
            IntentFilter agentFinishFilter = new IntentFilter(agentFinishAction);
            appContext.registerReceiver(agentFinishReceiver, agentFinishFilter, Context.RECEIVER_EXPORTED);

            String ctxName = TouchPointHelper.touchPointPluginName(name);
            TouchPointContextManager.getContext(ctxName).putTouchPointReceiver(filterAction, tpInstanceReceiver);
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
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
