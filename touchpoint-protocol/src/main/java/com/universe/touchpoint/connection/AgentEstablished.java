package com.universe.touchpoint.connection;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.AgentSocket;
import com.universe.touchpoint.AgentConnection;

public class AgentEstablished implements AgentConnection {

    @Override
    public void onStateChange(AgentSocket socket, String actionClassName, Context context) {
    }

    @Override
    public void onStateChange(AgentSocket socket, Context context) {
        Log.i("AgentEstablished", "agent connected");
        socket.setState(new AgentClosed());
    }

}
