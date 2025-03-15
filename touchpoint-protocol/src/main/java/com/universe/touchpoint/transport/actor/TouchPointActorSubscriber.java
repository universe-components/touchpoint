package com.universe.touchpoint.transport.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.plan.ActionExecutionSelector;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.plan.ResultExchanger;

public class TouchPointActorSubscriber<M extends TouchPoint> extends AbstractBehavior<M> {

  private final Class<M> messageType;

  public TouchPointActorSubscriber(ActorContext<M> context, Class<M> messageType) {
    super(context);
    this.messageType = messageType;
  }

  public Behavior<M> create() {
    return Behaviors.setup(context -> this);
  }

  @Override
  public Receive<M> createReceive() {
    return newReceiveBuilder().onMessage(messageType, this::onMessageReceived).build();
  }

  public Behavior<M> onMessageReceived(M message) {
    String taskName = message.getContext().getBelongTask();
    TaskContext taskContext = message.getContext().getTaskContext();

    message =
        ((ActionExecutor<M, ?>) ActionExecutionSelector.getExecutor(message)).execute(message);
    new ResultExchanger().exchange(message, taskContext.getGoal(), taskName, Transport.ACTOR);
    return this;
  }
}
