package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.context.AgentContext;

public interface TouchPointRegistry {

    <C extends AgentContext> void registerReceiver(Context appContext, C context);

}
