package com.universe.touchpoint.security.tokenizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.touchpoint.security.Tokenizer;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;

public class JWT<T> implements Tokenizer<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecretKey key = Jwts.SIG.HS256.key().build();
    private static final String TOKEN_NAME = "Bearer_Token";
    private static final int EXPIRED_TIME = 3600000;

    @Override
    public String generateToken(T obj) {
        return Jwts.builder()
                .json(new JacksonSerializer<>(objectMapper))
                .claim(TOKEN_NAME, obj)
                .expiration(new Date(System.currentTimeMillis() + EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    @Override
    public T parseToken(String token) {
        Claims claims = Jwts.parser()
                .json(new JacksonDeserializer<>(objectMapper))
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return (T) claims.values().iterator().next();
    }

}
