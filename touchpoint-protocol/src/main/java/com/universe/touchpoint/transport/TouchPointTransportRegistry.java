package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;

public interface TouchPointTransportRegistry<C> {

    void init(Context context, C transportConfig);

    void register(Context context, AgentActionMetaInfo agentActionMetaInfo, String filter);


}
