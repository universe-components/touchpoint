package com.universe.touchpoint.guide;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.utils.StringUtils;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class RoleWorker {

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
    OperatorSelector.getOperator(actionMeta.getOperateType()).run(action);
  }
}
