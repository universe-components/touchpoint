package com.universe.touchpoint;

import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.mapping.CoordinatorConfigMapping;
import com.universe.touchpoint.config.mapping.SupervisorConfigMapping;
import com.universe.touchpoint.config.role.CoordinatorConfig;
import com.universe.touchpoint.config.role.SupervisorConfig;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.utils.AnnotationUtils;

public class TaskParticipant {

  public static CoordinatorConfig registerCoordinator(Class<?> actionClass, String actionName) {
    try {
      CoordinatorConfig coordinatorConfig =
          (CoordinatorConfig)
              AnnotationUtils.annotation2Config(
                  actionClass, CoordinatorConfigMapping.annotation2Config);
      if (coordinatorConfig == null) {
        return null;
      }
      TaskRoleExecutor.getInstance(coordinatorConfig.getTask())
          .registerExecutor(
              actionName, (RoleExecutor<?, ?>) actionClass.getConstructor().newInstance());
      return coordinatorConfig;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static SupervisorConfig registerSupervisor(Class<?> actionClass, String actionName) {
    try {
      SupervisorConfig supervisorConfig =
          (SupervisorConfig)
              AnnotationUtils.annotation2Config(
                  actionClass, SupervisorConfigMapping.annotation2Config);
      if (supervisorConfig == null) {
        return null;
      }
      TaskRoleExecutor.getInstance(supervisorConfig.getTask())
          .registerExecutor(
              actionName, (RoleExecutor<?, ?>) actionClass.getConstructor().newInstance());
      return supervisorConfig;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
