package com.universe.touchpoint.socket.protocol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import com.universe.touchpoint.utils.SerializeUtils;
import java.util.Objects;

public class AndroidBroadcastProtocol implements AgentSocketProtocol {

    @Override
    public void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, Context context, String filter) {
        Intent intent = new Intent(filter);
        intent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT, SerializeUtils.serializeToByteArray(stateContext));
        context.sendBroadcast(intent);
    }

    @Override
    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context, ActionRole role) {
        assert context != null;
        String task = context.getBelongTask();
        String socketFilter = TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, task, role.name());
        IntentFilter filter = new IntentFilter(TouchPointHelper.touchPointFilterName(socketFilter));
        appContext.registerReceiver(new AgentSocketStateListener<>(context), filter, Context.RECEIVER_EXPORTED);
    }

    @Override
    public void initialize(AgentSocketConfig socketConfig) {
    }

    public static class AgentSocketStateListener<C extends AgentContext> extends BroadcastReceiver {

        private final C context;

        public AgentSocketStateListener(C context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context appContext, Intent intent) {
            byte[] stateContextBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT);
            if (stateContextBytes == null) {
                return;
            }
            String task = TouchPointHelper.extractFilter(Objects.requireNonNull(intent.getAction()));
            new AgentSocketStateRouter<>().route(context, appContext, stateContextBytes, task);
        }

    }

}
