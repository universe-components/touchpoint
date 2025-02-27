package com.universe.touchpoint.monitor.action;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.MetricSyncerFactory;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;

import java.util.Map;

@TouchPointAction(
        name = "metrics_syncer",
        desc = "sync task and action metrics",
        toAgents = {"collect_metrics[]"})
public class MetricSyncer implements AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint, Context context) {
        String task = touchPoint.getContext().getTask();
        TaskMetric taskMetric = TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric();
        Map<String, ActionMetric> actionMetrics = TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetrics();
        MetricSyncerFactory.getSyncer(task).sendMetrics(Pair.create(taskMetric, actionMetrics), task, context);
        return touchPoint;
    }

}
