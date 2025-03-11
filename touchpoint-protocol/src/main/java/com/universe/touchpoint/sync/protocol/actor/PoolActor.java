package com.universe.touchpoint.sync.protocol.actor;

import com.universe.touchpoint.negotiation.AgentContext;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;

public class PoolActor extends AbstractBehavior<Object> {

    private final ActorRef<Object> receiverPool;

    public <C extends AgentContext> PoolActor(ActorContext<Object> context, C agentContext, String filter, String topic) {
        super(context);
        var router = Routers.pool(5, ActorReceiver.create(agentContext, filter, topic));
        receiverPool = context.spawn(router, "receiver-pool");
    }

    public static <C extends AgentContext> Behavior<Object> create(C agentContext, String filter, String topic) {
        return Behaviors.setup(context -> new PoolActor(context, agentContext, filter, topic));
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(Object.class, message -> {
                    receiverPool.tell(message);
                    return Behaviors.same();
                })
                .build();
    }

}
