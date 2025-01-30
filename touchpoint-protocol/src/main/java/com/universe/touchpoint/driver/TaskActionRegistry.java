package com.universe.touchpoint.driver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.TouchPointRegistry;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TaskActionRegistry implements TouchPointRegistry<ActionConfig> {

    @Override
    public void register(ActionConfig config, Context context) {
        for (int i = 0; i < config.getTaskProposers().length; i++) {
            Intent taskIntent = new Intent(TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_TASK_FILTER, config.getTaskProposers()[i]));

            taskIntent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_EVENT_NAME,
                    SerializeUtils.serializeToByteArray(config));
            context.sendBroadcast(taskIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_ROUTER_FILTER_NAME, Agent.getName()));
        context.registerReceiver(new TaskActionReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}

