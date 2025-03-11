package com.universe.touchpoint.sync;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.monitor.MetricReceiver;
import com.universe.touchpoint.rolemodel.AgentContextReceiver;
import com.universe.touchpoint.negotiation.AgentStateReceiver;

public class AgentReceiverSelector {

    public static AgentReceiver<?> selectReceiver(String filter) {
        return switch (filter) {
            case TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER -> new AgentStateReceiver();
            case TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER -> new AgentContextReceiver();
            case TouchPointConstants.METRIC_FILTER -> new MetricReceiver();
            default -> null;
        };
    }

}
