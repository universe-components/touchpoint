package com.universe.touchpoint.monitor.action.collector;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;

@TouchPointAction(
        name = "action_metric_collector",
        desc = "collect action prediction metrics",
        toAgents = {"collect_metrics[task_metric_collector]"})
public class ActionMetricCollector extends AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint, TouchPointContext context) {
        String task = context.getTask();
        String countAction = context.getActionContext().getCurrentAction();
        TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetric(countAction).incrementPredictionCount();
        return touchPoint;
    }

}
