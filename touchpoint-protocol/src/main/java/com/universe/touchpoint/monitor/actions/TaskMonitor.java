package com.universe.touchpoint.monitor.actions;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.api.checker.TaskChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.monitor.MonitorResult;
import com.universe.touchpoint.state.DriverState;
import com.universe.touchpoint.state.enums.TaskState;

public class TaskMonitor<T extends TouchPoint> implements TaskChecker<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, Context context) {
        TaskContext task = touchPoint.getContext().getTask();
        TaskMetricConfig metricConfig = ConfigManager.selectTaskMetricConfig(task.getTaskName());
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (task.getMetric().getRetryActionCount() > metricConfig.getMaxRetryActionCount()) {
            monitorResult.setState(new DriverState(
                    TaskState.NEED_REORDER_ACTION.getCode(),
                    "The task has too many action retries",
                    touchPoint.getHeader().getToAction().getActionName()));
            return monitorResult;
        }

        monitorResult.setState(new DriverState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
