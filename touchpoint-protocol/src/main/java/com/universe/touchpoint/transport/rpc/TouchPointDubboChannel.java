package com.universe.touchpoint.transport.rpc;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.driver.ResultProcessorAdapter;
import com.universe.touchpoint.transport.TouchPointRpcChannel;

import org.apache.dubbo.config.bootstrap.builders.ReferenceBuilder;

public class TouchPointDubboChannel extends TouchPointRpcChannel<DubboConfig> {

    public TouchPointDubboChannel(Context context, DubboConfig transportConfig) {
        super(context, transportConfig);
    }

    @Override
    public <T extends TouchPoint> boolean send(T touchpoint) {
        Class<?> touchPointService =
                (Class<?>) ReferenceBuilder.newBuilder()
                        .interfaceClass((transportConfig.interfaceClass))
                        .build()
                        .get();

        if (touchPointService != null) {
            java.lang.reflect.Method action = touchPointService.getDeclaredMethods()[0];
            // 调用方法action，传入 touchpoint 参数
            Object result = null;
            try {
                result = action.invoke(touchPointService.getDeclaredConstructor().newInstance(), touchpoint);
                assert result != null;
                ((AgentAction) touchpoint).setObservation(result.toString());
                ResultProcessorAdapter
                        .getProcessor(touchpoint, touchpoint.goal, null, null, null, null)
                        .process();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return true;
        } else {
            throw new IllegalStateException("touchPointService is null");
        }
    }

}
