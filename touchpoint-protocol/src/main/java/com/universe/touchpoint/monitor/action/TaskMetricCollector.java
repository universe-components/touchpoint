package com.universe.touchpoint.monitor.action;

import android.content.Context;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.context.TouchPointContextManager;

@TouchPointAction(
        name = "task_metric_collector",
        desc = "collect task metrics",
        toAgents = {"collect_metrics[]"})
public class TaskMetricCollector implements AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint, Context context) {
        String task = touchPoint.getContext().getTask();
        String countAction = touchPoint.getContext().getActionContext().getCurrentAction();
        int actionRetryCount = touchPoint.getContext().getActionContext().getActionMetric(countAction).getPredictionCount();
        if (actionRetryCount > 0) {
            TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric().addRetryActionCount(actionRetryCount);
        }
        return touchPoint;
    }

}
