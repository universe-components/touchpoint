package com.universe.touchpoint.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.utils.SerializeUtils;

public class TaskRegistry extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] action = intent.getByteArrayExtra(TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_TASK_EVENT_NAME, Agent.getName()));

        if (action != null) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.addTouchPointTaskAction(
                    SerializeUtils.deserializeFromByteArray(action, ActionConfig.class));
        }
    }

}