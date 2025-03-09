package com.universe.touchpoint.sync;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.negotiation.AgentContextReceiver;
import com.universe.touchpoint.rolemodel.AgentStateReceiver;

public class AgentReceiverSelector {

    public static AgentReceiver selectReceiver(String filter) {
        return switch (filter) {
            case TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER -> new AgentStateReceiver();
            case TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER -> new AgentContextReceiver();
            default -> null;
        };
    }

}
