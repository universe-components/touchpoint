package com.universe.touchpoint.transport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.AgentSocket;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointTransportConfigReceiver extends BroadcastReceiver {

    private final AgentSocket socket;

    public TouchPointTransportConfigReceiver(AgentSocket socket) {
        this.socket = socket;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] agentTransportConfig = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_EVENT_NAME);

        TransportConfig config = SerializeUtils.deserializeFromByteArray(agentTransportConfig, TransportConfig.class);
        if (config != null && config.config() instanceof RPCConfig) {
            AgentBuilder.getBuilder().getConfig().setTransportConfig(config);
        }
        socket.changeState(context);
    }

}
