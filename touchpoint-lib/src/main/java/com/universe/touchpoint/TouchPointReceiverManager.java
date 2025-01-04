package com.universe.touchpoint;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.channel.broadcast.TouchPointBroadcastReceiver;

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
    public void registerTouchPointReceiver(Context appContext, String name, String receiverClassName) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(receiverClassName);
            TouchPointReceiver<?> tpInstanceReceiver = (TouchPointReceiver<?>) tpInstanceReceiverClass.getConstructor().newInstance();

            Type[] interfaces = tpInstanceReceiverClass.getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            Type actualType = parameterizedType.getActualTypeArguments()[0];

            Class<?> touchPointClazz = Class.forName(actualType.getTypeName());
            Class<? extends TouchPoint> touchPointClass = touchPointClazz.asSubclass(TouchPoint.class);
            TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver = new TouchPointBroadcastReceiver<>(touchPointClass, appContext);

            IntentFilter filter = new IntentFilter(name);
            appContext.registerReceiver(tpReceiver, filter, Context.RECEIVER_EXPORTED);

            TouchPointContextManager.getContext().putTouchPointReceiver(name, tpInstanceReceiver);
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
