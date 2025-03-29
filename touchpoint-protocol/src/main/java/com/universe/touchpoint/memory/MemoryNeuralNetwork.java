package com.universe.touchpoint.memory;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.plan.PlanContext;
import com.universe.touchpoint.plan.ResultDispatcher;
import com.universe.touchpoint.security.TokenizerSelector;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.Objects;

public class MemoryNeuralNetwork<P> {

  public void firstActions(SocketRequest<P> request) {
    SocketProtocol protocol =
        Objects.requireNonNull(
                ConfigManager.selectAgentSocket(request.getContext().getBelongTask()))
            .getBindProtocol();
    ((AgentSyncProtocol<SocketRequest<P>>) AgentSyncProtocolSelector.selectProtocol(protocol))
        .send(request, TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER);
  }

  public <F extends TouchPoint> void nextAction(F from) {
    SocketProtocol protocol =
        Objects.requireNonNull(ConfigManager.selectAgentSocket(from.getContext().getBelongTask()))
            .getBindProtocol();
    String actionName = null;
    if (from instanceof AgentAction<?, ?>) {
      actionName = ((AgentAction<?, ?>) from).getActionName();
    } else if (from instanceof AgentFinish) {
      actionName = from.getHeader().getFromAction().getName();
    }
    SocketRequest<P> request = new SocketRequest<>();
    request.getContext().getActionContext().setCurrentAction(actionName);
    ((AgentSyncProtocol<SocketRequest<P>>) AgentSyncProtocolSelector.selectProtocol(protocol))
        .send(request, TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER);
  }

  public static class ActivityRecordReceiver<P> extends AgentReceiver<SocketRequest<P>> {
    @Override
    public <C extends AgentContext> void handleMessage(
        C context, SocketRequest<P> request, String topic) {
      String inputText = request.getContext().getActionContext().getCurrentAction();
      if (inputText == null) {
        inputText = request.getContext().getTaskContext().getGoal();
      }
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
