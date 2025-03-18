package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.rolemodel.coordinator.Operator;
import com.universe.touchpoint.utils.StringUtils;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public abstract class RoleWorker {

  protected static final Map<String, Operator> operatorMap = new ConcurrentHashMap<>();

  public static void init() {
    ServiceLoader.load(Operator.class)
        .forEach(
            operator -> {
              String operateType =
                  StringUtils.camelToUnderline(operator.getClass().getSimpleName(), true)
                      .toUpperCase();
              operatorMap.put(operateType, operator);
            });
  }

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
