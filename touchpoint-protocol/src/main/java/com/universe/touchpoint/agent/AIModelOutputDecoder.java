package com.universe.touchpoint.agent;

public interface AIModelOutputDecoder<O, C> {

    O run(String params, Class<C> clazz);

}
