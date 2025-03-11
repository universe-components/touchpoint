package com.universe.touchpoint.security;

public interface TokenProcessor<T> {

    String generateToken(T obj);

    T parseToken(String token);

}
