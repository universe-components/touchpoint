package com.universe.touchpoint.monitor.action.alarm;

import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.context.TaskState;

public class ActionMonitor<T extends TouchPoint> extends AgentActionExecutor<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, TouchPointContext context) {
        String ctxAction = context.getAction();
        String task = context.getTask();
        ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(ctxAction, task);
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetric(ctxAction).getPredictionCount() > metricConfig.getMaxPredictionCount()) {
            monitorResult.setState(new TouchPoint.TouchPointState(
                    TaskState.NEED_SWITCH_LANG_MODEL.getCode(),
                    "The AI model has too many prediction rounds and still hasn't provided a final result",
                    touchPoint.getHeader().getToAction().getName()));
            return monitorResult;
        }

        monitorResult.setState(new TouchPoint.TouchPointState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
