package com.universe.touchpoint.monitor.action.alarm;

import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.context.TaskState;

public class TaskMonitor<T extends TouchPoint> extends AgentActionExecutor<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, TouchPointContext context) {
        String task = context.getTask();
        TaskMetricConfig metricConfig = ConfigManager.selectTaskMetricConfig(task);
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric().getRetryActionCount() > metricConfig.getMaxRetryActionCount()) {
            monitorResult.setState(new TouchPoint.TouchPointState(
                    TaskState.NEED_REORDER_ACTION.getCode(),
                    "The task has too many action retries",
                    touchPoint.getHeader().getToAction().getName()));
            return monitorResult;
        }

        monitorResult.setState(new TouchPoint.TouchPointState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
