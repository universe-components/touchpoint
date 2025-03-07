package com.universe.touchpoint.monitor.action;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.MetricSyncerFactory;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;

import org.apache.commons.lang3.tuple.Pair;
import java.util.Map;

@TouchPointAction(
        name = "metrics_syncer",
        desc = "sync task and action metrics",
        toAgents = {"collect_metrics[]"})
public class MetricSyncer extends AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint, TouchPointContext context) {
        String task = context.getTask();
        TaskMetric taskMetric = TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric();
        Map<String, ActionMetric> actionMetrics = TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetrics();
        MetricSyncerFactory.getSyncer(task).sendMetrics(Pair.of(taskMetric, actionMetrics), task);
        return touchPoint;
    }

}
