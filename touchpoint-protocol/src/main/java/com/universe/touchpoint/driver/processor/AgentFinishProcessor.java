package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultDispatcher;
import com.universe.touchpoint.driver.ResultProcessor;

public class AgentFinishProcessor implements ResultProcessor<AgentFinish> {

    @Override
    public String process(AgentFinish result,
                          String goal, String task, Context context, Transport transport) {
        if (transport == Transport.DUBBO) {
            return result.getOutput();
        }

        ResultDispatcher.run(result, result.getHeader().getFromAction(), context);
        return null;
    }

}
