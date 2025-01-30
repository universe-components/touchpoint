package com.universe.touchpoint.agent;

import android.os.Build;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.utils.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class AgentActionManager {

    private static AgentActionManager actionManager;
    private static final Object lock = new Object();

    public static AgentActionManager getInstance() {
        synchronized (lock) {
            if (actionManager == null) {
                actionManager = new AgentActionManager();
            }
            return actionManager;
        }
    }

    public <T> AgentActionMetaInfo extractAndRegisterAction(
            String receiverClassName,
            AIModelConfig model,
            TransportConfig<T> transportConfig,
            String actionName,
            String agentName) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(receiverClassName);

            Type[] interfaces = tpInstanceReceiverClass.getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            Type actualType = parameterizedType.getActualTypeArguments()[0];

            String touchPointClassName = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                touchPointClassName = actualType.getTypeName();
            }
            Class<?> touchPointClazz = Class.forName(touchPointClassName);
            Class<? extends TouchPoint> touchPointClass = touchPointClazz.asSubclass(TouchPoint.class);

            AgentActionMetaInfo agentActionMetaInfo = new AgentActionMetaInfo(receiverClassName, touchPointClass, model, transportConfig);
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointAction(
                    TouchPointHelper.touchPointActionName(actionName, agentName), agentActionMetaInfo);

            return agentActionMetaInfo;
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends TouchPoint> T paddingActionInput(String actionName, String actionInput, String agentName) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        AgentActionMetaInfo agentActionMetaInfo = driverRegion.getTouchPointAction(TouchPointHelper.touchPointActionName(actionName, agentName));
        Class<T> inputClass = (Class<T>) agentActionMetaInfo.inputClass();

        // 分割输入
        String[] actionInputs = actionInput.split("\\|");

        try {
            T touchPointInstance = inputClass.getDeclaredConstructor().newInstance();
            Field[] fields = inputClass.getDeclaredFields();

            // 遍历字段并填充值
            for (int i = 0; i < fields.length && i < actionInputs.length; i++) {
                Field field = fields[i];
                field.setAccessible(true); // 确保可以访问私有字段
                Object value = ClassUtils.convertToFieldType(field.getType(), actionInputs[i]); // 转换为字段的类型
                field.set(touchPointInstance, value); // 设置字段值
            }

            // 返回填充后的对象
            return touchPointInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create or populate inputClass instance", e);
        }
    }

}
