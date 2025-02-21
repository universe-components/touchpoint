package com.universe.touchpoint.monitor.actions;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.api.checker.ActionChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.monitor.MonitorResult;
import com.universe.touchpoint.state.DriverState;
import com.universe.touchpoint.state.enums.TaskState;

public class ActionMonitor<T extends TouchPoint> implements ActionChecker<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, Context context) {
        String ctxAction = touchPoint.getContext().getAction();
        TaskContext task = touchPoint.getContext().getTask();
        ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(ctxAction, task.getTaskName());
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (touchPoint.getContext().getActionMetric(ctxAction).getPredictionCount() > metricConfig.getMaxPredictionCount()) {
            monitorResult.setState(new DriverState(
                    TaskState.NEED_SWITCH_AI_MODEL.getCode(),
                    "The AI model has too many prediction rounds and still hasn't provided a final result",
                    touchPoint.getHeader().getToAction().getActionName()));
            return monitorResult;
        }

        monitorResult.setState(new DriverState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
