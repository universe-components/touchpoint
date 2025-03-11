package com.universe.touchpoint.plan;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public interface ResultProcessor<R> {

  <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(
      R result, String goal, String task, Transport transport);
}
