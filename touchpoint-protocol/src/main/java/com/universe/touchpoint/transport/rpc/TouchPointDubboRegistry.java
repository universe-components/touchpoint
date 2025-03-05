package com.universe.touchpoint.transport.rpc;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.bootstrap.builders.ServiceBuilder;

public class TouchPointDubboRegistry implements TouchPointTransportRegistry<DubboConfig> {

    @Override
    public void init( DubboConfig transportConfig) {
        DubboBootstrap.getInstance()
                .application(transportConfig.getApplicationName())
                .registry(new RegistryConfig(transportConfig.getRegistryAddress()))
                .protocol(new ProtocolConfig(CommonConstants.TRIPLE, 50051))
                .start()
                .await();
    }

    @Override
    public void register(AgentActionMeta agentActionMeta, String previousAction, String task, boolean isRequested) {
        DubboConfig dubboConfig = (DubboConfig) agentActionMeta.getTransportConfig().config();
        try {
            Class<?> providerClass = Class.forName(agentActionMeta.getClassName());
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
