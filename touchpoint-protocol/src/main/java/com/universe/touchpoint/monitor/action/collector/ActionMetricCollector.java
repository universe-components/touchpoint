package com.universe.touchpoint.monitor.action.collector;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;

@TouchPointAction(
    name = "action_metric_collector",
    desc = "collect action prediction metrics",
    toAgents = {"collect_metrics[task_metric_collector]"})
public class ActionMetricCollector extends AgentActionExecutor<AgentAction<?, ?>, TouchPoint> {

  @Override
  public TouchPoint run(SocketRequest<AgentAction<?, ?>> request, TouchPointContext context) {
    String task = context.getBelongTask();
    String countAction = context.getActionContext().getCurrentAction();
    TouchPointContextManager.getTouchPointContext(task)
        .getActionContext()
        .getActionMetric(countAction)
        .incrementPredictionCount();
    return request.getBody();
  }
}
