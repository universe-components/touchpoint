package com.universe.touchpoint.router;

public interface Router<I, T> {

    T routeTo(I input);

}
