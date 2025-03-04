package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.ai.models.OpenVLA;

public abstract class ActionPredictor implements AgentActionExecutor<ImageData, OpenVLA.ActionRequest> {

    @Override
    public OpenVLA.ActionRequest run(ImageData imageData) {
        String goal = imageData.getContext().getTaskContext().getGoal();
        return new OpenVLA.ActionRequest(imageData.getData(), goal);
    }

}
