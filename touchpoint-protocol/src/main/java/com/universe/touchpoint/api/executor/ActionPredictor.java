package com.universe.touchpoint.api.executor;

import android.content.Context;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.api.RoleExecutor;

public abstract class ActionPredictor implements RoleExecutor<ImageData, OpenVLA.ActionRequest> {

    @Override
    public OpenVLA.ActionRequest run(ImageData imageData, Context context) {
        String goal = imageData.getContext().getTaskContext().getGoal();
        return new OpenVLA.ActionRequest(imageData.getData(), goal);
    }

}
