package com.universe.touchpoint.monitor.action.alarm;

import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.context.TouchPointState;
import com.universe.touchpoint.monitor.MonitorResult;
import com.universe.touchpoint.context.TaskState;

public class TaskMonitor<T extends TouchPoint> implements AgentActionExecutor<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint) {
        String task = touchPoint.getContext().getTask();
        TaskMetricConfig metricConfig = ConfigManager.selectTaskMetricConfig(task);
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TouchPointContextManager.getTouchPointContext(task).getTaskContext().getMetric().getRetryActionCount() > metricConfig.getMaxRetryActionCount()) {
            monitorResult.setState(new TouchPointState(
                    TaskState.NEED_REORDER_ACTION.getCode(),
                    "The task has too many action retries",
                    touchPoint.getHeader().getToAction().getName()));
            return monitorResult;
        }

        monitorResult.setState(new TouchPointState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
