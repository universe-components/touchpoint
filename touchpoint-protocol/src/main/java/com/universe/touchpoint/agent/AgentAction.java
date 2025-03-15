package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.meta.data.AgentActionMeta;

public class AgentAction<I, O> extends TouchPoint {

  private String actionName;
  private String thought;
  private O output;
  private SocketRequest<I> input;
  private AgentActionMeta meta;

  public AgentAction(String actionName, AgentActionMeta actionMeta, Header header, String task) {
    super(header, new TouchPointContext(task));
    this.actionName = actionName;
    this.meta = actionMeta;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public String getThought() {
    return thought;
  }

  public void setThought(String thought) {
    this.thought = thought;
  }

  public O getOutput() {
    return output;
  }

  public void setOutput(O output) {
    this.output = output;
  }

  public SocketRequest<I> getInput() {
    return input;
  }

  public void setInput(SocketRequest<I> input) {
    this.input = input;
  }

  public void setMeta(AgentActionMeta meta) {
    this.meta = meta;
  }

  public AgentActionMeta getMeta() {
    return meta;
  }
}
