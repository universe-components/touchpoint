package com.universe.touchpoint.agent;

public interface ActionInputEncoder<I, O, MC> {

    O run(I input, MC modelConfig);

}
