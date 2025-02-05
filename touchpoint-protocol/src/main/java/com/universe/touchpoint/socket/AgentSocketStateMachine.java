package com.universe.touchpoint.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.socket.handler.AIModelDistributedHandler;
import com.universe.touchpoint.socket.handler.TaskParticipantReadyHandler;
import com.universe.touchpoint.socket.handler.TaskReadyHandler;
import com.universe.touchpoint.socket.handler.TransportConfigDistributedHandler;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class AgentSocketStateMachine {

    private static final Object mLock = new Object();
    private static AgentSocketStateMachine mInstance;

    public static AgentSocketStateMachine getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AgentSocketStateMachine();
            }
            return mInstance;
        }
    }

    public void start(Context context, String task) {
        send(
            new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.TASK_READY),
            context,
            task
        );
    }

    public void send(AgentSocketStateContext<?> stateContext, Context context, String filterSuffix) {
        Intent intent = new Intent(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                        filterSuffix
                ));
        intent.putExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT, SerializeUtils.serializeToByteArray(stateContext));
        context.sendBroadcast(intent);
    }

    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context) {
        assert context != null;
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                        context.getBelongTask())
        );
        appContext.registerReceiver(new AgentSocketStateListener<>(context), filter, Context.RECEIVER_EXPORTED);
    }

    public static class AgentSocketStateListener<C extends AgentContext> extends BroadcastReceiver {

        private final C context;

        private static final Map<AgentSocketState, AgentSocketStateHandler<?>> stateHandlerMap = new HashMap<>();
        static {
            stateHandlerMap.put(AgentSocketState.TASK_READY, new TaskReadyHandler());
            stateHandlerMap.put(AgentSocketState.PARTICIPANT_READY, new TaskParticipantReadyHandler());
            stateHandlerMap.put(AgentSocketState.TRANSPORT_DISTRIBUTED, new TransportConfigDistributedHandler());
            stateHandlerMap.put(AgentSocketState.AI_MODEL_DISTRIBUTED, new AIModelDistributedHandler());
        }

        public AgentSocketStateListener(C context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context appContext, Intent intent) {
            byte[] stateContextBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT);
            if (stateContextBytes == null) {
                return;
            }
            try {
                AgentSocketStateContext<?> stateContext = SerializeUtils.deserializeFromByteArray(stateContextBytes, AgentSocketStateContext.class);
                AgentSocketStateHandler<?> stateHandler = stateHandlerMap.get(stateContext.getSocketState());
                assert stateHandler != null;
                Object ctx = stateContext.getContext();
                if (ctx == null) {
                    ctx = context;
                }
                AgentSocketState nextState = AgentSocketState.next(stateContext.getSocketState());
                if (nextState != null) {
                    String filterSuffix = TouchPointHelper.extractSuffixFromFilter(Objects.requireNonNull(intent.getAction()));
                    AgentSocketStateMachine.getInstance().send(new AgentSocketStateContext<>(
                            nextState, stateHandler.onStateChange(ctx, appContext, filterSuffix)
                    ), appContext, filterSuffix);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class AgentSocketStateContext<C> {

        private final AgentSocketState socketState;
        private C context;

        public AgentSocketStateContext (AgentSocketState socketState) {
            this.socketState = socketState;
        }

        public AgentSocketStateContext (AgentSocketState socketState, C context) {
            this.socketState = socketState;
            this.context = context;
        }

        public AgentSocketState getSocketState() {
            return socketState;
        }

        public C getContext() {
            return context;
        }

    }

}
