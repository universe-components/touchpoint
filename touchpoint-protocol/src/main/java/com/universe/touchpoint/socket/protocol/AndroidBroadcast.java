package com.universe.touchpoint.socket.protocol;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.utils.SerializeUtils;

public class AndroidBroadcast implements AgentSocketProtocol {

    @Override
    public void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, Context context, String filterSuffix) {
        Intent intent = new Intent(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                        filterSuffix
                ));
        intent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT, SerializeUtils.serializeToByteArray(stateContext));
        context.sendBroadcast(intent);
    }

    @Override
    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context) {
        assert context != null;
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                        context.getBelongTask())
        );
        appContext.registerReceiver(new AgentSocketStateMachine.AgentSocketStateListener<>(context), filter, Context.RECEIVER_EXPORTED);
    }

}
