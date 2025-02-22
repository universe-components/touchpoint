package com.universe.touchpoint.api.executor;

import android.content.Context;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.api.RoleExecutor;

public interface TouchPointExecutor<I extends TouchPoint, O extends TouchPoint> extends RoleExecutor<I, O> {

    O run(I touchPoint, Context context);

}
