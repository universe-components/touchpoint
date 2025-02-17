package com.universe.touchpoint.rolemodel;

import static com.universe.touchpoint.state.enums.TaskState.NEED_CHECK_ACTION;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleConstants;
import com.universe.touchpoint.monitor.ActionMonitor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RoleExecutor<Executor> {

    private final Map<String, Executor> executorMap = new HashMap<>();
    {
        executorMap.put(RoleConstants.ACTION_CAPABILITY_CHECKER, (Executor) new ActionMonitor<>());
    }

    public void registerExecutor(String action, Executor executor) {
        executorMap.put(action, executor);
    }

    public Executor getExecutor(String action) {
        return executorMap.get(action);
    }

    public Object run(AgentAction<?, ?> action, Executor executor) {
        try {
            Class<?>[] paramsType = executor.getClass().getMethod("run").getParameterTypes();
            Method method = executor.getClass().getMethod("run", paramsType);

            if (action.getInput().getState().getCode() == NEED_CHECK_ACTION.getCode()) {
                return method.invoke(executor, action.getInput(), action);
            } else {
                return method.invoke(executor, action.getInput());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
