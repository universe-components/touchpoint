package com.universe.touchpoint.memory;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.memory.selector.LongTermMemorySelector;
import com.universe.touchpoint.memory.selector.ShortTermMemorySelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import java.util.List;

public abstract class ActionSelector {

  private String task;
  private SocketRequest<?> request;

  public static AgentActionMeta select(
      String task, SocketRequest<?> request, Socket.TaskCallbackListener callbackListener) {
    List<AgentActionMeta> agentActionMetas = new LongTermMemorySelector().select();
    if (agentActionMetas == null) {
      agentActionMetas = new ShortTermMemorySelector<>(request, callbackListener, task).select();
    }
    return agentActionMetas.get(0);
  }

  public String getTask() {
    return task;
  }

  public SocketRequest<?> getRequest() {
    return request;
  }

  public abstract List<AgentActionMeta> select();
}
