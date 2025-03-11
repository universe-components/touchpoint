package com.universe.touchpoint.sync.protocol.actor;

import com.universe.touchpoint.negotiation.AgentContext;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;

public class PoolActor<M> extends AbstractBehavior<M> {

    private final ActorRef<M> receiverPool;
    private final Class<M> messageType;

    public <C extends AgentContext> PoolActor(ActorContext<M> context, C agentContext, String filter, String topic, Class<M> messageType) {
        super(context);
        var router = Routers.pool(5, new ActorReceiver<>(context, agentContext, filter, topic, messageType).create());
        this.messageType = messageType;
        receiverPool = context.spawn(router, "receiver-pool");
    }

    public static <C extends AgentContext, M> Behavior<M> create(C agentContext, String filter, String topic, Class<M> messageType) {
        return Behaviors.setup(context -> new PoolActor<>(context, agentContext, filter, topic, messageType));
    }

    @Override
    public Receive<M> createReceive() {
        return newReceiveBuilder()
                .onMessage(messageType, message -> {
                    receiverPool.tell(message);
                    return Behaviors.same();
                })
                .build();
    }

}
