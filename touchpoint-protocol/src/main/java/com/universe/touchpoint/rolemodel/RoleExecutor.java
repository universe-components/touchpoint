package com.universe.touchpoint.rolemodel;

import static com.universe.touchpoint.state.enums.TaskState.NEED_CHECK_ACTION;
import static com.universe.touchpoint.state.enums.TaskState.NEED_CHECK_DATA;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.annotations.role.ActionRole;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RoleExecutor<Executor> {

    private final Map<String, Executor> executorMap = new HashMap<>();

    public void registerExecutor(String action, Executor executor) {
        executorMap.put(action, executor);
    }

    public Executor getExecutor(String action) {
        return executorMap.get(action);
    }

    public Object run(TouchPoint touchPoint, Executor executor) {
        try {
            Class<?>[] paramsType = executor.getClass().getMethod("run").getParameterTypes();
            Method method = executor.getClass().getMethod("run", paramsType);

            if (!(touchPoint instanceof AgentAction) && !(touchPoint instanceof AgentFinish)) {
                return method.invoke(executor, touchPoint);
            }

            if (touchPoint instanceof AgentAction<?, ?>) {
                if (((AgentAction<?, ?>) touchPoint).getMeta().role() == ActionRole.SUPERVISOR) {
                    if (((AgentAction<?, ?>) touchPoint).getActionInput().getState().getCode() == NEED_CHECK_ACTION.getCode()) {
                        return method.invoke(executor, ((AgentAction<?, ?>) touchPoint).getActionInput(), touchPoint);
                    } else if (((AgentAction<?, ?>) touchPoint).getActionInput().getState().getCode() == NEED_CHECK_DATA.getCode()) {
                        return method.invoke(executor, touchPoint);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
