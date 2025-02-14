package com.universe.touchpoint.socket;

import android.content.Context;
import com.universe.touchpoint.context.AgentContext;

public interface AgentSocketStateHandler<I, O> {

    <C extends AgentContext> O onStateChange(I input, C agentContext, Context context, String filterSuffix);

}
