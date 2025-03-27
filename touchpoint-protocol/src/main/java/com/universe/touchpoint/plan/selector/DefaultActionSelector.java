package com.universe.touchpoint.plan.selector;

import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ActionSelector;
import java.util.List;

public class DefaultActionSelector<I> implements ActionSelector<I> {

  @Override
  public List<AgentActionMeta> select(String task, SocketRequest<I> request) {
    return ActionGraphBuilder.getTaskGraph(task).getFirstNodes();
  }
}
