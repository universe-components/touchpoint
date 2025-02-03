package com.universe.touchpoint.connection;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.AgentConnection;
import com.universe.touchpoint.AgentSocket;

public class AgentClosed implements AgentConnection {

    @Override
    public void onStateChange(AgentSocket socket, Context context) {
        Log.i("AgentClosed", "agent disconnected");
    }

    @Override
    public void onStateChange(AgentSocket socket, String actionClassName, Context context) {
    }

}
