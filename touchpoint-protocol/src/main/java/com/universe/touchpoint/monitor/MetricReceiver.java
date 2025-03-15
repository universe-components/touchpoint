package com.universe.touchpoint.monitor;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;
import com.universe.touchpoint.sync.AgentReceiver;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class MetricReceiver extends AgentReceiver<Pair<TaskMetric, Map<String, ActionMetric>>> {

  @Override
  public <C extends AgentContext> void handleMessage(
      C context, Pair<TaskMetric, Map<String, ActionMetric>> metricPair, String topic) {
    TouchPointContextManager.getTouchPointContext(context.getBelongTask())
        .getTaskContext()
        .setMetric(metricPair.getLeft());
    TouchPointContextManager.getTouchPointContext(context.getBelongTask())
        .getActionContext()
        .setActionMetrics(metricPair.getRight());
  }
}
