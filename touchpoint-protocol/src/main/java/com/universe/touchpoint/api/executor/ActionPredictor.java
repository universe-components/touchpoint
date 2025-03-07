package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.context.TouchPointContext;

public abstract class ActionPredictor extends AgentActionExecutor<ImageData, OpenVLA.ActionRequest> {

    @Override
    public OpenVLA.ActionRequest run(ImageData imageData, TouchPointContext context) {
        String goal = context.getTaskContext().getGoal();
        return new OpenVLA.ActionRequest(imageData.getData(), goal);
    }

}
