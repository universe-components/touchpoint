package com.universe.touchpoint.api;

public interface RoleExecutor<I, O> {

    O run(I input);

}
