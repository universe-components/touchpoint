package com.universe.touchpoint;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.connection.AgentListening;

import java.util.List;

public class AgentSocket {

    private static final Object mLock = new Object();
    private static AgentSocket mInstance;

    private AgentConnection state;

    private AgentSocket() {
        state = new AgentListening();
    }

    public static AgentSocket getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AgentSocket();
            }
            return mInstance;
        }
    }

    public void connect(Context context, List<Pair<String, List<Object>>> receiverFilterPair) {
        ActionReporter.getInstance("router").report(receiverFilterPair, context);
    }

    public void changeState(Context context) {
        state.onStateChange(this, context);
    }

    public void changeState(Context context, String actionClassName) {
        state.onStateChange(this, actionClassName, context);
    }

    public void setState(AgentConnection state) {
        this.state = state;
    }

}
