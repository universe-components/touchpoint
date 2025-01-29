package com.universe.touchpoint.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TaskRegistry extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] action = intent.getByteArrayExtra(TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_TASK_EVENT_NAME, Agent.getName()));

        if (action != null) {
            TouchPointContextManager.getContext(Agent.getName())
                    .addTouchPointTaskAction(SerializeUtils.deserializeFromByteArray(action, ActionConfig.class));
        }
    }

}