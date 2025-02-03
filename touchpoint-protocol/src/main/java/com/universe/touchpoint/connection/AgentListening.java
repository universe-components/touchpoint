package com.universe.touchpoint.connection;

import android.content.Context;

import com.universe.touchpoint.AgentBroadcaster;
import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.AgentConnection;
import com.universe.touchpoint.AgentSocket;

public class AgentListening implements AgentConnection {

    @Override
    public void onStateChange(AgentSocket socket, Context context) {
        AgentBroadcaster.getInstance("transportConfig").send(
                AgentBuilder.getBuilder().getConfig().getTransportConfig(), context);
        socket.setState(new AgentLinkEstablished());
    }

    @Override
    public void onStateChange(AgentSocket socket, String actionClassName, Context context) {
    }

}
