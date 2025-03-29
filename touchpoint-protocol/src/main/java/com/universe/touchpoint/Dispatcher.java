package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.task.OperateType;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ResultDispatcher;
import com.universe.touchpoint.security.TokenizerSelector;
import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

  public static <P, F> F dispatch(
      String action, SocketRequest<P> params, Socket.TaskCallbackListener callbackListener) {
    List<F> finalResult = new ArrayList<>();
    String task = confirmTask(action, params);
    AgentActionMeta actionMeta = ActionSelector.firstAction(task, params, callbackListener);
    AgentAction<P, ?> agentAction =
        new AgentAction<>(
            actionMeta.getName(), actionMeta, new TouchPoint.Header(actionMeta), task);
    agentAction.setContext(
        (TouchPointContext) TokenizerSelector.getTokenizer("jwt").parseToken(params.getToken()));
    agentAction.getHeader().setCallbackListener(callbackListener);
    agentAction.setInput(params);
    finalResult.add(ResultDispatcher.run(agentAction, actionMeta));
    return finalResult.get(0);
  }

  public static <P> String confirmTask(String action, SocketRequest<P> params) {
    String task;
    AgentActionMeta firstActionMeta =
        ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAction(action);
    if (firstActionMeta.getOperateType() == OperateType.PROPOSE_TASK) {
      task = action;
    } else {
      task = (String) params.getActionBody().getTarget();
    }
    return task;
  }
}
