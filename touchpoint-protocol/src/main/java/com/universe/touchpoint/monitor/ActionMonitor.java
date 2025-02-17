package com.universe.touchpoint.monitor;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.checker.ActionChecker;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.state.DriverState;
import com.universe.touchpoint.state.enums.TaskState;

public class ActionMonitor<T extends TouchPoint> implements ActionChecker<T, MonitorResult> {

    @Override
    public MonitorResult run(T touchPoint, AgentAction<?, ?> action) {
        ActionMetricConfig metricConfig = ConfigManager.selectActionMetricConfig(action.getAction(), action.getTask());
        MonitorResult monitorResult = new MonitorResult();

        assert metricConfig != null;
        if (action.getMetric().getPredictionCount().get() > metricConfig.getMaxPredictionCount()) {
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
