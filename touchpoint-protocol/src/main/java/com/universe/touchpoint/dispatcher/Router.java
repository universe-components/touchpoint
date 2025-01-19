package com.universe.touchpoint.dispatcher;

public interface Router<I, T> {

    T routeTo(I input);

}
