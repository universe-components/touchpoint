package com.universe.touchpoint.plan;

import android.content.Context;

public abstract class ActionExecutor<T> {

    public void execute(T touchPoint, Context context) {
        if (beforeRun(touchPoint, context)) {
            Object runResult = run(touchPoint, context);
            if (runResult != null) {
                afterRun(touchPoint, runResult, context);
            }
        }
     }

    public abstract boolean beforeRun(T touchPoint, Context context);

    public abstract <O> O run(T touchPoint, Context context);

    public abstract <RunResult> void afterRun(T touchPoint, RunResult runResult, Context context);

}
