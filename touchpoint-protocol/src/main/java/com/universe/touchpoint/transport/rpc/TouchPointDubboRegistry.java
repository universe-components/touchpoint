package com.universe.touchpoint.transport.rpc;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.bootstrap.builders.ServiceBuilder;

public class TouchPointDubboRegistry implements TouchPointTransportRegistry {

    @Override
    public void register(Context context, AgentActionMetaInfo agentActionMetaInfo, String[] filters) {
        DubboConfig dubboConfig = (DubboConfig) agentActionMetaInfo.transportConfig().config();
        try {
            Class<?> providerClass = Class.forName(agentActionMetaInfo.name());
            DubboBootstrap.getInstance()
                    .service(ServiceBuilder.newBuilder()
                            .interfaceClass(dubboConfig.interfaceClass)
                            .ref(providerClass.getConstructor().newInstance())
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
