package com.universe.touchpoint.config.transport;

public record TransportConfig<C>(Transport transportType, C config) {}