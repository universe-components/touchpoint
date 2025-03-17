package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.annotations.role.OperateType;
import com.universe.touchpoint.utils.StringUtils;

public class OperatorSelector {

  public static Operator getOperator(OperateType operateType) {
    String operateClassName = StringUtils.convertToCamelCase(operateType.name(), true);
    try {
      String operatePackage = "com.universe.touchpoint.rolemodel.coordinator.operator";
      String suffix = "Operator";
      return (Operator)
          Class.forName(String.join(".", operatePackage, operateClassName) + suffix)
              .getDeclaredConstructor()
              .newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
