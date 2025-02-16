package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultDispatcher;
import com.universe.touchpoint.driver.ResultProcessor;

public class AgentFinishProcessor<T extends TouchPoint> extends ResultProcessor<AgentFinish, T> {

    public AgentFinishProcessor(AgentFinish result,
                                String goal, String task, Context context, Transport transport) {
        super(result, goal, task, context, transport);
    }

    @Override
    public String process() {
        if (transportType == Transport.DUBBO) {
            return result.getOutput();
        }

        ResultDispatcher.run(result, result.getHeader().getFromAction(), context);
        return null;
    }

}
