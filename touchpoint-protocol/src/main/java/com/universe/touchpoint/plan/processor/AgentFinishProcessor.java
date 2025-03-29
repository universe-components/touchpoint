package com.universe.touchpoint.plan.processor;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ResultProcessor;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class AgentFinishProcessor implements ResultProcessor<AgentFinish<?>> {
  @Override
  public <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(
      AgentFinish<?> result, String goal, String task, Transport transport) {
    if (transport == Transport.DUBBO) {
      return Pair.of(null, result);
    }
    List<AgentActionMeta> nextActions = ActionSelector.nextAction(result, false);
    return Pair.of(null, new AgentFinish<>(result.getOutput(), nextActions.get(0)));
  }
}
