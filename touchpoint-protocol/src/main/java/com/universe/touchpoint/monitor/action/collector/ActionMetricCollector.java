package com.universe.touchpoint.monitor.action.collector;

import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;

@TouchPointAction(
        name = "action_metric_collector",
        desc = "collect action prediction metrics",
        toAgents = {"collect_metrics[task_metric_collector]"})
public class ActionMetricCollector implements AgentActionExecutor<TouchPoint, TouchPoint> {

    @Override
    public TouchPoint run(TouchPoint touchPoint) {
        String task = touchPoint.getContext().getTask();
        String countAction = touchPoint.getContext().getActionContext().getCurrentAction();
        TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetric(countAction).incrementPredictionCount();
        return touchPoint;
    }

}
