package com.universe.touchpoint.sync.protocol.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.sync.AgentReceiverSelector;
import java.util.Objects;

public class ActorReceiver<C extends AgentContext, M> extends AbstractBehavior<M> {

  private final String filter;
  private final String topic;
  private final C agentContext;
  private final Class<M> messageType;

  public ActorReceiver(
      ActorContext<M> context, C agentContext, String filter, String topic, Class<M> messageType) {
    super(context);
    this.agentContext = agentContext;
    this.filter = filter;
    this.topic = topic;
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
    ((AgentReceiver<M>) Objects.requireNonNull(AgentReceiverSelector.selectReceiver(filter)))
        .handleMessage(agentContext, message, topic, messageType);
    return this;
  }
}
