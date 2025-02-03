package com.universe.touchpoint.connection;

import android.content.Context;

import com.universe.touchpoint.AgentReporter;
import com.universe.touchpoint.AgentSocket;
import com.universe.touchpoint.AgentConnection;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.mapping.ActionConfigMapping;
import com.universe.touchpoint.utils.AnnotationUtils;

public class AgentLinkEstablished implements AgentConnection {

    @Override
    public void onStateChange(AgentSocket connection, String actionClassName, Context context) {
        ActionConfig actionConfig;
        try {
            actionConfig = (ActionConfig) AnnotationUtils.annotation2Config(
                    Class.forName(actionClassName),
                    ActionConfigMapping.annotation2Config
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert actionConfig != null;
        AgentReporter.getInstance("taskAction").report(actionConfig, context);
        connection.setState(new AgentEstablished());
    }

    @Override
    public void onStateChange(AgentSocket connection, Context context) {
    }

}
