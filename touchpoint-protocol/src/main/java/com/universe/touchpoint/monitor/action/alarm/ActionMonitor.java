package com.universe.touchpoint.monitor.action.alarm;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.checker.DefaultChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.context.TaskState;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;

public class ActionMonitor implements DefaultChecker<AgentAction<?, ?>> {

  @Override
  public Boolean run(SocketRequest<AgentAction<?, ?>> action, TouchPointContext context) {
    String ctxAction = action.getBody().getActionName();
    String task = context.getBelongTask();
    ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(ctxAction, task);
    MonitorResult monitorResult = new MonitorResult();

    assert metricConfig != null;
    if (TouchPointContextManager.getTouchPointContext(task)
            .getActionContext()
            .getActionMetric(ctxAction)
            .getPredictionCount()
        > metricConfig.getMaxPredictionCount()) {
      return false;
    }

    monitorResult.setState(new TouchPoint.TouchPointState(TaskState.OK.getCode(), "success"));
    return true;
  }
}
