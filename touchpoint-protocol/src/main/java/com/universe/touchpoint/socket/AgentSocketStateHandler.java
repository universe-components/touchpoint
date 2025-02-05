package com.universe.touchpoint.socket;

import android.content.Context;

public interface AgentSocketStateHandler<O> {

    O onStateChange(Object input, Context context, String filterSuffix);

}
