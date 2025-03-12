package com.universe.touchpoint.transport;

public interface TouchPointChannel<T, R> {

  R send(T touchpoint);
}
