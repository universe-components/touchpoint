package com.universe.touchpoint.plan.executor;

import android.content.Context;
import com.universe.touchpoint.api.executor.TouchPointExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class DefaultActionExecutor<T extends TouchPoint> extends ActionExecutor<T> {

    @Override
    public boolean beforeRun(T touchPoint, Context context) {
        return false;
    }

    @Override
    public <O> O run(T touchPoint, Context context) {
        String taskName = touchPoint.getContext().getTask();
        TouchPointExecutor<T, ?> touchPointExecutor = (TouchPointExecutor<T, ?>) TaskRoleExecutor.getInstance(taskName)
                .getExecutor(touchPoint.getHeader().getFromAction().getActionName());
        touchPointExecutor.run(touchPoint, context);
        return null;
    }

    @Override
    public <RunResult> T afterRun(T touchPoint, RunResult runResult, Context context) {
        return null;
    }

}
