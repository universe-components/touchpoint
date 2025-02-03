package com.universe.touchpoint.connection;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.AgentSocket;
import com.universe.touchpoint.AgentConnection;

public class AgentEstablished implements AgentConnection {

    @Override
    public void onStateChange(AgentSocket connection, String actionClassName, Context context) {
    }

    @Override
    public void onStateChange(AgentSocket connection, Context context) {
        Log.i("AgentEstablished", "agent connected");
    }

}
