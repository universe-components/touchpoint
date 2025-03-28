package com.universe.touchpoint.memory;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.plan.ActionSelector;
import com.universe.touchpoint.plan.PlanContext;
import com.universe.touchpoint.plan.ResultDispatcher;
import com.universe.touchpoint.security.TokenizerSelector;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.List;
import java.util.Objects;

public class RelevanceActionSelector<P> implements ActionSelector<P> {

  public RelevanceActionSelector(
      SocketRequest<P> params, Socket.TaskCallbackListener callbackListener, String task) {
    AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
    ((AgentSyncProtocol<SocketResponse>)
            AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol()))
        .registerReceiver(
            new PlanContext<>(params, callbackListener, task),
            TouchPointConstants.TOUCH_POINT_ACTIVITY_RESPONSE_FILTER,
            RoleType.OWNER,
            SocketResponse.class);
  }

  @Override
  public List<AgentActionMeta> select(String task, SocketRequest<P> request) {
    SocketProtocol protocol =
        Objects.requireNonNull(
                ConfigManager.selectAgentSocket(request.getContext().getBelongTask()))
            .getBindProtocol();
    ((AgentSyncProtocol<SocketRequest<P>>) AgentSyncProtocolSelector.selectProtocol(protocol))
        .send(request, TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER);
    return null;
  }

  public static class ActivityRecordReceiver<P> extends AgentReceiver<SocketRequest<P>> {
    @Override
    public <C extends AgentContext> void handleMessage(
        C context, SocketRequest<P> request, String topic) {
      String inputText = request.getContext().getTaskContext().getGoal();
      AgentActionMeta actionMeta =
          ((MetaRegion) TouchPointMemory.getRegion(Region.META))
              .getTouchPointAction(((TaskActionContext) context).getAction());

      ActivityGraphBuilder.getActivityGraph(context.getBelongTask()).addNode(inputText);
      ActivityGraphBuilder.getActivityGraph(context.getBelongTask()).addNode(actionMeta.getDesc());
      double sim =
          ActivityGraphBuilder.getActivityGraph(context.getBelongTask())
              .addEdge(inputText, actionMeta.getDesc());
      if (sim >= 0.8) {
        SocketProtocol protocol =
            Objects.requireNonNull(
                    ConfigManager.selectAgentSocket(request.getContext().getBelongTask()))
                .getBindProtocol();
        ((AgentSyncProtocol<SocketResponse<AgentActionMeta, ?>>)
                AgentSyncProtocolSelector.selectProtocol(protocol))
            .send(
                new SocketResponse<>(actionMeta),
                TouchPointConstants.TOUCH_POINT_ACTIVITY_RESPONSE_FILTER);
      }
    }
  }

  public static class ActivityResponseReceiver<O, P>
      extends AgentReceiver<SocketResponse<AgentActionMeta, O>> {

    @Override
    public <C extends AgentContext> void handleMessage(
        C context, SocketResponse<AgentActionMeta, O> response, String topic) {
      PlanContext<P> planContext = (PlanContext<P>) context;
      AgentAction<P, ?> agentAction =
          new AgentAction<>(
              response.getBody().getName(),
              response.getBody(),
              new TouchPoint.Header(response.getBody()),
              context.getBelongTask());
      agentAction.setContext(
          (TouchPointContext)
              TokenizerSelector.getTokenizer("jwt").parseToken(planContext.getParams().getToken()));
      agentAction.getHeader().setCallbackListener(planContext.getCallbackListener());
      agentAction.setInput(planContext.getParams());
      ResultDispatcher.run(agentAction, response.getBody());
    }
  }
}
