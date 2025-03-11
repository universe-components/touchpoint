package com.universe.touchpoint.monitor.action;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.tuple.Pair;

@TouchPointAction(
    name = "metrics_syncer",
    desc = "sync task and action metrics",
    toAgents = {"collect_metrics[]"})
public class MetricSyncer extends AgentActionExecutor<AgentAction<?, ?>, TouchPoint> {

  @Override
  public TouchPoint run(AgentAction<?, ?> action, TouchPointContext context) {
    String task = context.getTask();
    SocketProtocol protocol =
        Objects.requireNonNull(ConfigManager.selectAgentSocket(task)).getBindProtocol();
    TaskMetric taskMetric =
        TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric();
    Map<String, ActionMetric> actionMetrics =
        TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetrics();
    ((AgentSyncProtocol<Pair<TaskMetric, Map<String, ActionMetric>>>)
            AgentSyncProtocolSelector.selectProtocol(protocol))
        .send(Pair.of(taskMetric, actionMetrics), task);
    return action;
  }
}
