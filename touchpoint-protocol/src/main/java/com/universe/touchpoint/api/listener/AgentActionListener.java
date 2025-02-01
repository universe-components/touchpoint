package com.universe.touchpoint.api.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointListener;

public interface AgentActionListener<Req extends TouchPoint, Resp extends TouchPoint> extends TouchPointListener<Req, Resp> {

    Resp onReceive(Req action, Context context);

}
