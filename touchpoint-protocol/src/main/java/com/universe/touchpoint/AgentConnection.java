package com.universe.touchpoint;

import android.content.Context;

public interface AgentConnection {

    void onStateChange(AgentSocket connection, Context context);
    void onStateChange(AgentSocket connection, String actionClassName, Context context);

}
