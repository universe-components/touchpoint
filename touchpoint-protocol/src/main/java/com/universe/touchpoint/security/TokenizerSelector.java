package com.universe.touchpoint.security;

public class TokenizerSelector {

  public static <T> Tokenizer<T> getTokenizer(String name) {
    try {
      String tokenizerClassName =
          "com.universe.touchpoint.security.tokenizer." + name.toUpperCase();
      return (Tokenizer<T>)
          Class.forName(tokenizerClassName).getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
