package com.universe.touchpoint.config.transport;

public record TransportConfig<T>(Transport transportType, T config) {}