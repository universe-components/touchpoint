package com.universe.touchpoint.monitor.action.collector;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;

@TouchPointAction(
        name = "task_metric_collector",
        desc = "collect task metrics",
        toAgents = {"collect_metrics[metrics_syncer]"})
public class TaskMetricCollector extends AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint, TouchPointContext context) {
        String task = context.getTask();
        String countAction = context.getActionContext().getCurrentAction();
        int actionRetryCount = touchPoint.getContext().getActionContext().getActionMetric(countAction).getPredictionCount();
        if (actionRetryCount > 0) {
            TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric().addRetryActionCount(actionRetryCount);
        }
        return touchPoint;
    }

}
