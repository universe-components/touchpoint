package com.universe.touchpoint.monitor.actions;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContext;
import com.universe.touchpoint.api.checker.TaskChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.monitor.MonitorResult;
import com.universe.touchpoint.monitor.TaskMetricManager;
import com.universe.touchpoint.state.DriverState;
import com.universe.touchpoint.state.enums.TaskState;

public class TaskMonitor<T extends TouchPoint> implements TaskChecker<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, ActionGraph actionGraph, TouchPointContext context) {
        TaskMetricConfig metricConfig = ConfigManager.selectTaskMetricConfig(context.task());
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TaskMetricManager.getTaskMetric(context.task()).getRetryActionCount() > metricConfig.getMaxRetryActionCount()) {
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
