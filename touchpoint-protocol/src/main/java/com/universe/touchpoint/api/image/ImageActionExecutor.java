package com.universe.touchpoint.api.image;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.MediaEncodeExecutor;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;

public abstract class ImageActionExecutor implements RoleExecutor<ImageData, Double[]> {

    @Override
    public Double[] run(ImageData imageData, Context context) {
        String goal = imageData.getContext().getTaskContext().getGoal();
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        AgentActionMetaInfo actionMetaInfo = driverRegion.getTouchPointAction(imageData.getContext().getActionContext().getCurrentAction());
        return MediaEncodeExecutor.encode(imageData.getData(), goal, actionMetaInfo.getVisionModel(), actionMetaInfo.getVisionLangModel(), actionMetaInfo.getModel());
    }

}
