package com.universe.touchpoint.transport.rpc;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.plan.ResultExchanger;
import com.universe.touchpoint.transport.TouchPointRpcChannel;
import org.apache.dubbo.config.bootstrap.builders.ReferenceBuilder;

public class TouchPointDubboChannel<I, O, T extends AgentAction<I, O>>
    extends TouchPointRpcChannel<T, O, DubboConfig> {

  public TouchPointDubboChannel(DubboConfig transportConfig) {
    super(transportConfig);
  }

  @Override
  public O send(T touchpoint) {
    Class<?> touchPointService =
        (Class<?>)
            ReferenceBuilder.newBuilder()
                .interfaceClass((transportConfig.interfaceClass))
                .build()
                .get();

    if (touchPointService != null) {
      java.lang.reflect.Method action = touchPointService.getDeclaredMethods()[0];
      // 调用方法action，传入 touchpoint 参数
      O result;
      try {
        result =
            (O)
                action.invoke(
                    touchPointService.getDeclaredConstructor().newInstance(),
                    touchpoint.getInput());
        assert result != null;
        touchpoint.setOutput(result);
        return new ResultExchanger()
            .exchange(touchpoint, touchpoint.getContext().getTaskContext().getGoal(), null, null);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new IllegalStateException("touchPointService is null");
    }
  }
}
