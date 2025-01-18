package com.universe.touchpoint;

public interface Router<I, T> {

    T routeTo(I input);

}
