package com.universe.touchpoint.monitor.action.fetcher;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskMetricFetcher<T extends TouchPoint> extends AgentActionExecutor<T, TaskMetric> {

  @Override
  public TaskMetric run(SocketRequest<T> input, TouchPointContext context) {
    return TouchPointContextManager.getTouchPointContext(
            input.getBody().getContext().getBelongTask())
        .getTaskContext()
        .getMetric();
  }
}
