package com.universe.touchpoint.memory;

import java.util.Set;

public interface Homomorphism<GT1, GT2> {

  Set<GT2> mapping(GT1 group1Element, Set<GT2> group2Elements);
}
