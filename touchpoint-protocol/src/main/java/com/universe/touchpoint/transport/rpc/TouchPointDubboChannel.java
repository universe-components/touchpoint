package com.universe.touchpoint.transport.rpc;

import android.content.Context;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.plan.ResultExchanger;
import com.universe.touchpoint.transport.TouchPointRpcChannel;

import org.apache.dubbo.config.bootstrap.builders.ReferenceBuilder;

public class TouchPointDubboChannel extends TouchPointRpcChannel<DubboConfig> {

    public TouchPointDubboChannel(Context context, DubboConfig transportConfig) {
        super(context, transportConfig);
    }

    @Override
    public <I extends TouchPoint, O extends TouchPoint> String send(I touchpoint) {
        Class<?> touchPointService =
                (Class<?>) ReferenceBuilder.newBuilder()
                        .interfaceClass((transportConfig.interfaceClass))
                        .build()
                        .get();

        if (touchPointService != null) {
            java.lang.reflect.Method action = touchPointService.getDeclaredMethods()[0];
            // 调用方法action，传入 touchpoint 参数
            Object result;
            try {
                result = action.invoke(touchPointService.getDeclaredConstructor().newInstance(), touchpoint);
                assert result != null;
                ((AgentAction<?, O>) touchpoint).setOutput((O) result);
                return ResultExchanger.exchange(touchpoint, touchpoint.getContext().getTaskContext().getGoal(), null, null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalStateException("touchPointService is null");
        }
    }

}
