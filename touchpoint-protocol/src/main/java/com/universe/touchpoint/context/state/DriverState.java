package com.universe.touchpoint.context.state;

import com.universe.touchpoint.context.TouchPointState;

public class DriverState extends TouchPointState {

    public DriverState(int code, String message) {
        super(code, message);
    }

    public DriverState(int code, String message, String action) {
        super(code, message, action);
    }

}
