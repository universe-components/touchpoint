package com.universe.touchpoint.context.state;

import com.universe.touchpoint.context.TouchPointState;

public class TransportState extends TouchPointState {

    public TransportState(int code, String message) {
        super(code, message);
    }

    public TransportState(int code, String message, String action) {
        super(code, message, action);
    }

}
