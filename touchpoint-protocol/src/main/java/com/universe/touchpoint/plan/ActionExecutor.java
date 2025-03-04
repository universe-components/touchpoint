package com.universe.touchpoint.plan;

public abstract class ActionExecutor<T, O> {

    public T execute(T touchPoint) {
        beforeRun(touchPoint);
        O runResult = run(touchPoint);
        if (runResult != null) {
            return afterRun(touchPoint, runResult);
        }
        return null;
     }

    public abstract void beforeRun(T touchPoint);

    public abstract O run(T touchPoint);

    public abstract T afterRun(T touchPoint, O runResult);

}
