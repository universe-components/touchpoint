package com.universe.touchpoint.transport.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;
import com.universe.touchpoint.TouchPoint;

public class PoolActor<M extends TouchPoint> extends AbstractBehavior<M> {

  private final ActorRef<M> receiverPool;
  private final Class<M> messageType;

  public PoolActor(ActorContext<M> context, Class<M> messageType) {
    super(context);
    var router = Routers.pool(5, new TouchPointActorSubscriber<>(context, messageType).create());
    this.messageType = messageType;
    receiverPool = context.spawn(router, "receiver-pool");
  }

  public static <M extends TouchPoint> Behavior<M> create(Class<M> messageType) {
    return Behaviors.setup(context -> new PoolActor<>(context, messageType));
  }

  @Override
  public Receive<M> createReceive() {
    return newReceiveBuilder()
        .onMessage(
            messageType,
            message -> {
              receiverPool.tell(message);
              return Behaviors.same();
            })
        .build();
  }
}
