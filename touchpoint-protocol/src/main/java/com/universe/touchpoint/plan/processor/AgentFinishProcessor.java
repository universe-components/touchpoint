package com.universe.touchpoint.plan.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ResultProcessor;

import java.util.List;

public class AgentFinishProcessor implements ResultProcessor<AgentFinish> {

    @Override
    public Pair<List<AgentAction<?, ?>>, AgentFinish> process(AgentFinish result, String goal, String task, Context context, Transport transport) {
        if (transport == Transport.DUBBO) {
            return Pair.create(null, result);
        }
        return null;
    }

}
