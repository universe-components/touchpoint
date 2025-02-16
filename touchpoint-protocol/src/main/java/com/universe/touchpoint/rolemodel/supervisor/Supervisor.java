package com.universe.touchpoint.rolemodel.supervisor;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;

public class Supervisor {

    public <E> void execute(AgentAction<?, ?> agentAction, String task) {
        E supervisor = (E) RoleExecutorFactory.getInstance(task).getExecutor(agentAction.getActionInput().getState().getAction());
        Object supervisedResult = RoleExecutorFactory.getInstance(task).run(agentAction, supervisor);
        if (supervisedResult instanceof Boolean && !(Boolean) supervisedResult) {
            throw new RuntimeException(String.format("ActionSupervisor run failed：action[%s] is not passed", agentAction.getAction()));
        }
    }

}
