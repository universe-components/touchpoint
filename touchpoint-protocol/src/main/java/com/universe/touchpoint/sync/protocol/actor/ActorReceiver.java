package com.universe.touchpoint.sync.protocol.actor;

import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.sync.AgentReceiverSelector;
import java.util.Objects;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ActorReceiver<C extends AgentContext> extends AbstractBehavior<Object> {

    private final String filter;
    private final String topic;
    private final C agentContext;

    public ActorReceiver(ActorContext<Object> context, C agentContext, String filter, String topic) {
        super(context);
        this.agentContext = agentContext;
        this.filter = filter;
        this.topic = topic;
    }

    public static <C extends AgentContext> Behavior<Object> create(C agentContext, String filter, String topic) {
        return Behaviors.setup(context -> new ActorReceiver<>(context, agentContext, filter, topic));
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder().onMessage(Object.class, this::onMessageReceived).build();
    }

    public Behavior<Object> onMessageReceived(Object message) {
        Objects.requireNonNull(AgentReceiverSelector.selectReceiver(filter)).handleMessage(agentContext, message, topic);
        return this;
    }

}
