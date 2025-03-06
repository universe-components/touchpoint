package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.TouchPoint;

public interface AgentActionExecutor<Req extends TouchPoint, Resp extends TouchPoint> extends RoleExecutor<Req, Resp> {
}
