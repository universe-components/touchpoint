package com.universe.touchpoint.api;

import com.universe.touchpoint.agent.AgentActionMetaInfo;

import java.util.List;
import java.util.Map;

public interface ActionCoordinator {

    void run(Map<AgentActionMetaInfo, List<AgentActionMetaInfo>> actionGraph);

}
