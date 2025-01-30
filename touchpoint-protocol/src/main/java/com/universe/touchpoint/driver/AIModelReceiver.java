package com.universe.touchpoint.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.utils.SerializeUtils;

public class AIModelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        byte[] aiModelConfig = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_AI_MODEL_CONFIG_EVENT_NAME);

        AIModelConfig config = SerializeUtils.deserializeFromByteArray(aiModelConfig, AIModelConfig.class);
        if (config != null) {
            AgentBuilder.getBuilder().getConfig().setModelConfig(config);
        }
    }

}
