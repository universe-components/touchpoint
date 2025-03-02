package com.universe.touchpoint.monitor.action.alarm;

import android.content.Context;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.context.TouchPointState;
import com.universe.touchpoint.monitor.MonitorResult;
import com.universe.touchpoint.context.TaskState;

public class ActionMonitor<T extends TouchPoint> implements AgentActionExecutor<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, Context context) {
        String ctxAction = touchPoint.getContext().getAction();
        String task = touchPoint.getContext().getTask();
        ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(ctxAction, task);
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (TouchPointContextManager.getTouchPointContext(task).getActionContext().getActionMetric(ctxAction).getPredictionCount() > metricConfig.getMaxPredictionCount()) {
            monitorResult.setState(new TouchPointState(
                    TaskState.NEED_SWITCH_LANG_MODEL.getCode(),
                    "The AI model has too many prediction rounds and still hasn't provided a final result",
                    touchPoint.getHeader().getToAction().getActionName()));
            return monitorResult;
        }

        monitorResult.setState(new TouchPointState(TaskState.OK.getCode(), "success"));
        return new MonitorResult();
    }

}
