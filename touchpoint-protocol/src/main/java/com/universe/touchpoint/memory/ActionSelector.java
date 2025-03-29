package com.universe.touchpoint.memory;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.memory.selector.LongTermMemorySelector;
import com.universe.touchpoint.memory.selector.ShortTermMemorySelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import java.util.List;

public abstract class ActionSelector {

  private String task;
  private SocketRequest<?> request;

  public static AgentActionMeta firstAction(
      String task, SocketRequest<?> request, Socket.TaskCallbackListener callbackListener) {
    List<AgentActionMeta> agentActionMetas = new LongTermMemorySelector().firstActions();
    if (agentActionMetas == null) {
      agentActionMetas =
          new ShortTermMemorySelector<>(request, callbackListener, task).firstActions();
    }
    return agentActionMetas.get(0);
  }

  public static <F extends TouchPoint> List<AgentActionMeta> nextAction(F from, boolean isCalling) {
    List<AgentActionMeta> agentActionMetas = new LongTermMemorySelector().nextActions(from);
    if (agentActionMetas == null && isCalling) {
      agentActionMetas = new ShortTermMemorySelector<>().nextActions(from);
    }
    return agentActionMetas;
  }

  public String getTask() {
    return task;
  }

  public SocketRequest<?> getRequest() {
    return request;
  }

  public abstract List<AgentActionMeta> firstActions();

  public abstract <F extends TouchPoint> List<AgentActionMeta> nextActions(F from);
}
