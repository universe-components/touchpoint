package com.universe.touchpoint.transport;

import com.universe.touchpoint.agent.meta.AgentActionMeta;

public interface TouchPointTransportRegistry<C> {

    void init(C transportConfig);

    void register(AgentActionMeta agentActionMeta, String filter, String task, boolean isRequested);


}
