package com.universe.touchpoint.plan;

import android.content.Context;

public abstract class ActionExecutor<T> {

    public T execute(T touchPoint, Context context) {
        if (beforeRun(touchPoint, context)) {
            Object runResult = run(touchPoint, context);
            if (runResult != null) {
                return afterRun(touchPoint, runResult, context);
            }
        }
        return null;
     }

    public abstract boolean beforeRun(T touchPoint, Context context);

    public abstract <O> O run(T touchPoint, Context context);

    public abstract <RunResult> T afterRun(T touchPoint, RunResult runResult, Context context);

}
