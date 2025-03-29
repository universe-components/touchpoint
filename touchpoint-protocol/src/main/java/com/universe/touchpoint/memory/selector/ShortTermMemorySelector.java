package com.universe.touchpoint.memory.selector;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.memory.MemoryNeuralNetwork;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.PlanContext;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.List;

public class ShortTermMemorySelector<I> extends ActionSelector {

  public ShortTermMemorySelector() {}

  public ShortTermMemorySelector(
      SocketRequest<I> params, Socket.TaskCallbackListener callbackListener, String task) {
    AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
    ((AgentSyncProtocol<SocketResponse>)
            AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol()))
        .registerReceiver(
            new PlanContext<>(params, callbackListener, task),
            TouchPointConstants.TOUCH_POINT_ACTIVITY_RESPONSE_FILTER,
            RoleType.OWNER,
            SocketResponse.class);
  }

  @Override
  public List<AgentActionMeta> firstActions() {
    new MemoryNeuralNetwork<I>().firstActions((SocketRequest<I>) getRequest());
    return null;
  }

  @Override
  public <F extends TouchPoint> List<AgentActionMeta> nextActions(F from) {
    new MemoryNeuralNetwork<I>().nextAction(from);
    return null;
  }
}
