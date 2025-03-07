package com.universe.touchpoint.api;

import com.universe.touchpoint.context.TouchPointContext;

public interface RoleExecutor<I, O> {

    O run(I input, TouchPointContext context);

}
