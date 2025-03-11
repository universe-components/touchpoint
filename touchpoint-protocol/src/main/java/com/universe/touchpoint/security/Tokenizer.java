package com.universe.touchpoint.security;

public interface Tokenizer<T> {

  String generateToken(T obj);

  T parseToken(String token);
}
