package com.universe.touchpoint.config.role;

public abstract class RoleConfig {

  public RoleConfig(String scope) {
    this.scope = scope;
  }

  protected String scope;

  public String getScope() {
    return scope;
  }
}
