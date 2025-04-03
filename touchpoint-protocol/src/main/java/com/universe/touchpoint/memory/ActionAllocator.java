package com.universe.touchpoint.memory;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.meta.data.TaskMeta;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class ActionAllocator {

  private final List<AgentActionMeta> actionMetaList = new CopyOnWriteArrayList<>();
  private Function<TaskMeta, AgentActionMeta> homomorphicMapping;

  public void allocateActions(TaskMeta taskMeta) {
    AgentActionMeta agentActionMeta = homomorphicMapping.apply(taskMeta);
    if (agentActionMeta != null) {
      actionMetaList.add(agentActionMeta);
    }
  }

  public List<AgentActionMeta> getActionMetaList() {
    return actionMetaList;
  }

  public void addActionMeta(AgentActionMeta agentActionMeta) {
    actionMetaList.add(agentActionMeta);
  }
}
