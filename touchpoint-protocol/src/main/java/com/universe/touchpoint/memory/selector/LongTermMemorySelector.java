package com.universe.touchpoint.memory.selector;

import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import java.util.List;

public class LongTermMemorySelector extends ActionSelector {

  @Override
  public List<AgentActionMeta> select() {
    return ActionGraphBuilder.getTaskGraph(getTask()).getFirstNodes();
  }
}
