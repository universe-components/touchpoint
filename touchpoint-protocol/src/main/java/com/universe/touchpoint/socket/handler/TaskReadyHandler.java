package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.mapping.ActionConfigMapping;
import com.universe.touchpoint.utils.AnnotationUtils;

public class TaskReadyHandler implements AgentSocketStateHandler<ActionConfig> {

    @Override
    public ActionConfig onStateChange(Object actionCtx, Context context, String task) {
        try {
            return (ActionConfig) AnnotationUtils.annotation2Config(
                    Class.forName(((TaskActionContext) actionCtx).getActionName()),
                    ActionConfigMapping.annotation2Config
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
