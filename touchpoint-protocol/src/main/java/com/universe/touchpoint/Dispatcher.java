package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ResultDispatcher;
import com.universe.touchpoint.security.TokenizerSelector;
import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

  public static <P, F> F dispatch(
      String action, SocketRequest<P> params, Socket.TaskCallbackListener callbackListener) {
    List<F> finalResult = new ArrayList<>();
    AgentActionMeta firstActionMeta =
        ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAction(action);
    String task;
    if (firstActionMeta.getRoleModel().getRole() == ActionRole.PROPOSER) {
      task = action;
    } else {
      task = (String) params.getActionBody().getTarget();
    }
    ActionGraphBuilder.getTaskGraph(task)
        .getFirstNodes()
        .forEach(
            actionMeta -> {
              AgentAction<P, ?> agentAction =
                  new AgentAction<>(
                      actionMeta.getName(), actionMeta, new TouchPoint.Header(actionMeta), task);
              agentAction.setContext(
                  (TouchPointContext)
                      TokenizerSelector.getTokenizer("jwt").parseToken(params.getToken()));
              agentAction.getHeader().setCallbackListener(callbackListener);
              agentAction.setInput(params);
              finalResult.add(ResultDispatcher.run(agentAction, actionMeta));
            });
    return finalResult.get(0);
  }
}
