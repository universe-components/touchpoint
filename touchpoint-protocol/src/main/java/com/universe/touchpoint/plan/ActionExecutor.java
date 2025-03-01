package com.universe.touchpoint.plan;

import android.content.Context;

public abstract class ActionExecutor<T, O> {

    public T execute(T touchPoint, Context context) {
        if (beforeRun(touchPoint, context)) {
            O runResult = run(touchPoint, context);
            if (runResult != null) {
                return afterRun(touchPoint, runResult, context);
            }
        }
        return null;
     }

    public abstract boolean beforeRun(T touchPoint, Context context);

    public abstract O run(T touchPoint, Context context);

    public abstract T afterRun(T touchPoint, O runResult, Context context);

}
