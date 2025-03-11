package com.universe.touchpoint.security.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universe.touchpoint.security.TokenProcessor;

import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;

public class JWTProcessor<T> implements TokenProcessor<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    @Override
    public String generateToken(T obj) {
        return Jwts.builder()
                .json(new JacksonSerializer<>(objectMapper))
                .claim("token", obj)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
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
