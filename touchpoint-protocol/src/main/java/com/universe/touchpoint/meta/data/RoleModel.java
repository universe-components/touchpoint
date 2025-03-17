package com.universe.touchpoint.meta.data;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.role.RoleConfig;

public class RoleModel<C extends RoleConfig> {

  private ActionRole role;
  private C config;

  public RoleModel(ActionRole role, C config) {
    this.role = role;
    this.config = config;
  }

  public ActionRole getRole() {
    return role;
  }

  public void setRole(ActionRole role) {
    this.role = role;
  }

  public C getConfig() {
    return config;
  }

  public void setConfig(C config) {
    this.config = config;
  }
}
