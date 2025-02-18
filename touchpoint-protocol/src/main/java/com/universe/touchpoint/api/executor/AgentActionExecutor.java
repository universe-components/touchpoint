package com.universe.touchpoint.api.executor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointExecutor;

public interface AgentActionExecutor<Req extends TouchPoint, Resp extends TouchPoint> extends TouchPointExecutor<Req, Resp> {

    Resp run(Req action, Context context);

}
