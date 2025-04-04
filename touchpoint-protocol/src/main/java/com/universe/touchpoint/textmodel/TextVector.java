package com.universe.touchpoint.textmodel;

import java.util.Map;
import java.util.Set;

public record TextVector(Map<String, Double> TFIDF, Set<String> allWords) {}
