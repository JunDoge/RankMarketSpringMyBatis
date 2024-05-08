package com.market.rank.config.jwt;

public class CustomJwtExpiredException extends RuntimeException {
    public CustomJwtExpiredException(String msg) {
        super(msg);
    }
}