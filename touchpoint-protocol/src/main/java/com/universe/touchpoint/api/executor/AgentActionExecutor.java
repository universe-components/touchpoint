package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.context.TouchPoint;

public interface AgentActionExecutor<Req extends TouchPoint, Resp extends TouchPoint> extends TouchPointExecutor<Req, Resp> {
}
