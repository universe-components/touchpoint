package com.universe.touchpoint.monitor.action.fetcher;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskMetricFetcher<T extends TouchPoint> implements RoleExecutor<T, TaskMetric> {

    @Override
    public TaskMetric run(T input) {
        return TouchPointContextManager.getTouchPointContext(input.getContext().getTask()).getTaskContext().getMetric();
    }

}
