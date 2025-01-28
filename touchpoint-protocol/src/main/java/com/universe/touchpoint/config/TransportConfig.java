package com.universe.touchpoint.config;

public record TransportConfig<T>(Transport transportType, T config) {}