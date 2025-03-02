package com.universe.touchpoint.plan.executor;

import android.content.Context;

import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class DefaultActionExecutor<T extends TouchPoint, O> extends ActionExecutor<T, O> {

    @Override
    public void beforeRun(T touchPoint, Context context) {
    }

    @Override
    public O run(T touchPoint, Context context) {
        String taskName = touchPoint.getContext().getTask();
        RoleExecutor<T, ?> touchPointExecutor = (RoleExecutor<T, ?>) TaskRoleExecutor.getInstance(taskName)
                .getExecutor(touchPoint.getHeader().getFromAction().getActionName());
        touchPointExecutor.run(touchPoint, context);
        return null;
    }

    @Override
    public T afterRun(T touchPoint, O runResult, Context context) {
        return null;
    }

}
