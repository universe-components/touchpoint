package com.universe.touchpoint.agent;

public interface ModelOutputDecoder<O, C> {

    O run(String params, Class<C> clazz);

}
