package com.universe.touchpoint.socket.selector;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.socket.AgentSocketReceiver;
import com.universe.touchpoint.socket.receiver.AgentContextReceiver;
import com.universe.touchpoint.socket.receiver.AgentStateReceiver;

public class AgentSocketReceiverSelector {

    public static AgentSocketReceiver selectReceiver(String filter) {
        return switch (filter) {
            case TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER -> new AgentStateReceiver();
            case TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER -> new AgentContextReceiver();
            default -> null;
        };
    }

}
