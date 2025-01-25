package com.universe.touchpoint.channel.rpc;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.channel.TouchPointChannel;

import org.apache.dubbo.config.ReferenceConfig;

public class TouchPointDubboChannel implements TouchPointChannel {

    @Override
    public <T extends TouchPoint> boolean send(T touchpoint) {
        ReferenceConfig<TouchPointService> reference = new ReferenceConfig<>();
        reference.setInterface(TouchPointService.class);
        reference.setTimeout(3000); // 设置超时
        reference.setRetries(2); // 设置重试次数
        reference.setUrl("dubbo://localhost:20880"); // 设置Dubbo服务的地址

        TouchPointService touchPointService = reference.get();
        return touchPointService.invoke(touchpoint);
    }

    public interface TouchPointService {
        <T> boolean invoke(T touchpoint);
    }

}
