package com.universe.touchpoint.driver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TaskManager {

    public static void registerTaskAction(ActionConfig actionConfig, Context context) {
        for (int i = 0; i < actionConfig.getTaskProposers().length; i++) {
            Intent taskIntent = new Intent(TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_TASK_FILTER, actionConfig.getTaskProposers()[i]));

            taskIntent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_EVENT_NAME,
                    SerializeUtils.serializeToByteArray(actionConfig));
            context.sendBroadcast(taskIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void registerTaskRegistry(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME, Agent.getName()));
        context.registerReceiver(new TaskRegistry(), filter, Context.RECEIVER_EXPORTED);
    }

}

