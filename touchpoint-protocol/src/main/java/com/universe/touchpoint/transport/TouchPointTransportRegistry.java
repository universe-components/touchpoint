package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMeta;

public interface TouchPointTransportRegistry {

    void register(Context context, AgentActionMeta agentActionMeta, String[] filters);


}
