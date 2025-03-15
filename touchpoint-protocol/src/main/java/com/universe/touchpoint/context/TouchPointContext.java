package com.universe.touchpoint.context;

import com.universe.touchpoint.api.operator.OperateMethod;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import java.util.Map;

public class TouchPointContext extends AgentContext {

  private TaskContext taskContext = new TaskContext();
  private final ActionContext actionContext = new ActionContext();
  private OperateMethod operateMethod;
  private Map<String, Object> extContext;
  private String token;

  public TouchPointContext(String task) {
    super(task);
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TaskContext getTaskContext() {
    return taskContext;
  }

  public void setTaskContext(TaskContext taskContext) {
    this.taskContext = taskContext;
  }

  public OperateMethod getOperateModel() {
    return operateMethod;
  }

  public void setOperateModel(OperateMethod operateMethod) {
    this.operateMethod = operateMethod;
  }

  public ActionContext getActionContext() {
    return actionContext;
  }

  public ActionGraph getActionGraph() {
    return ActionGraphBuilder.getTaskGraph(belongTask);
  }

  public void addExtContext(String name, Object context) {
    extContext.put(name, context);
  }

  public Object getExtContext(String name) {
    return extContext.get(name);
  }
}
