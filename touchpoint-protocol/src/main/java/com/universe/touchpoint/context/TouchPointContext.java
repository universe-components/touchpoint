package com.universe.touchpoint.context;

import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import java.util.Map;

public class TouchPointContext {

    private final String task;
    private TaskContext taskContext = new TaskContext();
    private final ActionContext actionContext = new ActionContext();
    private String action;
    private Map<String, Object> extContext;

    public TouchPointContext(String task) {
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    public TaskContext getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(TaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionContext getActionContext() {
        return actionContext;
    }

    public ActionGraph getActionGraph() {
        return ActionGraphBuilder.getTaskGraph(task);
    }

    public void addExtContext(String name, Object context) {
        extContext.put(name, context);
    }

    public Object getExtContext(String name) {
        return extContext.get(name);
    }

}
