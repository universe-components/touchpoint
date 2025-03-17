package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.utils.StringUtils;

public abstract class RoleWorker {

  public static void run(AgentAction<?, ?> action) {
    if (action.getInput().getActionBody() == null) {
      return;
    }
    String nextAction = action.getInput().getActionBody().getAction();
    AgentActionMeta actionMeta =
        ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAction(nextAction);
    String role = actionMeta.getRoleModel().getRole().name();
    String roleWorkerClass = StringUtils.convertToCamelCase(role, true);
    try {
      String rolePackage = "com.universe.touchpoint.rolemodel." + role.toLowerCase();
      RoleWorker roleWorker =
          (RoleWorker)
              Class.forName(String.join(".", rolePackage, roleWorkerClass))
                  .getDeclaredConstructor()
                  .newInstance();
      roleWorker.execute(action);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public abstract <I, O> void execute(AgentAction<I, O> action);
}
