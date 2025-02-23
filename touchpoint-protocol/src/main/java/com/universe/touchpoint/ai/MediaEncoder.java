package com.universe.touchpoint.ai;

public interface MediaEncoder<I, O, MC> {

    O run(I input, MC modelConfig);

}
