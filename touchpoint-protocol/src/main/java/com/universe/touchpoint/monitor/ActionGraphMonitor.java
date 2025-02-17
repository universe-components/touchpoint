package com.universe.touchpoint.monitor;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.checker.ActionGraphChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.state.DriverState;
import com.universe.touchpoint.state.enums.TaskState;

public class ActionGraphMonitor<T extends TouchPoint> implements ActionGraphChecker<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, String task) {
        TaskMetricConfig metricConfig = ConfigManager.selectTaskMetricConfig(task);
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TaskMetricManager.getTaskMetric(task).getRetryActionCount().get() > metricConfig.getMaxRetryActionCount()) {
            monitorResult.setState(new DriverState(
                    TaskState.NEED_SWITCH_ACTION.getCode(),
                    "The task has too many action retries",
                    touchPoint.getHeader().getToAction().getActionName()));
            return monitorResult;
        }

        monitorResult.setState(new DriverState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
