package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ResultDispatcher;
import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

  public static <P, F> F dispatch(
      String task, SocketRequest<P> params, Socket.TaskCallbackListener callbackListener) {
    List<F> finalResult = new ArrayList<>();
    ActionGraphBuilder.getTaskGraph(task)
        .getFirstNodes()
        .forEach(
            actionMeta -> {
              AgentAction<P, ?> action =
                  new AgentAction<>(
                      actionMeta.getName(), actionMeta, new TouchPoint.Header(actionMeta), task);
              MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
              action
                  .getContext()
                  .getTaskContext()
                  .setGoal(metaRegion.getTouchPointAction(task).getDesc());
              action.getHeader().setCallbackListener(callbackListener);
              if (params != null) {
                action.setInput(params);
              }
              finalResult.add(ResultDispatcher.run(action, actionMeta));
            });
    return finalResult.get(0);
  }
}
