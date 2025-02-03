package com.universe.touchpoint;

import android.content.Context;

public interface AgentConnection {

    void onStateChange(AgentSocket socket, Context context);
    void onStateChange(AgentSocket socket, String actionClassName, Context context);

}
