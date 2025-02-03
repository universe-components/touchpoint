package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;

public interface TouchPointTransportRegistry {

    void init(Context context, Object transportConfig);

    void register(Context context, AgentActionMetaInfo agentActionMetaInfo, String[] filters);


}
