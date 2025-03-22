package com.universe.touchpoint.api;

import com.universe.touchpoint.context.TouchPointContext;

public interface RoleExecutor<I, O> {

  SocketResponse<O, ?> run(SocketRequest<I> input, TouchPointContext context);
}
