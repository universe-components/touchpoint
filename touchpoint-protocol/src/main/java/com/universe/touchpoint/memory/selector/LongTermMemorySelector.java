package com.universe.touchpoint.memory.selector;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.router.Router;
import java.util.List;

public class LongTermMemorySelector extends ActionSelector {

  @Override
  public List<AgentActionMeta> firstActions() {
    return ActionGraphBuilder.getTaskGraph(getTask()).getFirstNodes();
  }

  @Override
  public <F extends TouchPoint> List<AgentActionMeta> nextActions(F from) {
    return Router.route(from, true);
  }
}
