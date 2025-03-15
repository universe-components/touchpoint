package com.universe.touchpoint.monitor.action.alarm;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.checker.DefaultActionChecker;
import com.universe.touchpoint.api.operator.OperateMethod;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.context.TaskState;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;

public class ActionMonitor implements DefaultActionChecker {

  @Override
  public Boolean run(SocketRequest<OperateMethod> operateMethod, TouchPointContext context) {
    String ctxAction = operateMethod.getBody().getTarget();
    String task = context.getBelongTask();
    ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(ctxAction, task);
    MonitorResult monitorResult = new MonitorResult();

    assert metricConfig != null;
    if (TouchPointContextManager.getTouchPointContext(task)
            .getActionContext()
            .getActionMetric(ctxAction)
            .getPredictionCount()
        > metricConfig.getMaxPredictionCount()) {
      SocketRequest<OperateMethod> operateMethodRequest =
          new SocketRequest<>(new OperateMethod(ctxAction, "switch_ai_model"));
      new Socket("switch_ai_model").send(operateMethodRequest);
      return false;
    }

    monitorResult.setState(new TouchPoint.TouchPointState(TaskState.OK.getCode(), "success"));
    return true;
  }
}
