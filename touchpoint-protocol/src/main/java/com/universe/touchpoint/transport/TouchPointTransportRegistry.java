package com.universe.touchpoint.transport;

import com.universe.touchpoint.meta.data.AgentActionMeta;

public interface TouchPointTransportRegistry<C> {

    void init(C transportConfig);

    void register(AgentActionMeta agentActionMeta, String filter, String task, boolean isRequested);


}
