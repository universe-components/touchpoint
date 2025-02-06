package com.universe.touchpoint.socket;

import android.content.Context;
import com.universe.touchpoint.context.AgentContext;

public interface AgentSocketStateHandler<O> {

    <C extends AgentContext> O onStateChange(Object input, C agentContext, Context context, String filterSuffix);

}
