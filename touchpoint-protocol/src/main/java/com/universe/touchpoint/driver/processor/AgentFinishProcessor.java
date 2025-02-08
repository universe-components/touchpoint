package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessor;
import com.universe.touchpoint.router.RouteTable;

public class AgentFinishProcessor<T extends TouchPoint> extends ResultProcessor<AgentFinish, T> {

    public AgentFinishProcessor(AgentFinish result,
                                String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transport) {
        super(result, goal, task, tpReceiver, context, transport);
    }

    @Override
    public String process() {
        if (RouteTable.getInstance().getPredecessors(result.getHeader().getFromAction()) == null) {
            if (tpReceiver != null) {
                tpReceiver.onReceive((T) result, context);
                return null;
            }
        }
        if (transportType == Transport.DUBBO) {
            return result.getOutput();
        }

        TouchPointContextManager.generateTouchPoint(result, goal).finish();
        return null;
    }

}
